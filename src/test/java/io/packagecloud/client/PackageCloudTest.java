package io.packagecloud.client;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simpleframework.http.Part;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit test for simple App.
 */
public class PackageCloudTest {

    private PackageCloud pcloud;

    @BeforeClass
    public static void setUp() throws Exception {
        Container container = new PackageCloudApp();
        Server server = new ContainerServer(container);

        Connection connection = new SocketConnection(server);
        SocketAddress address = new InetSocketAddress(8080);
        connection.connect(address);
    }

    @Before
    public void beforeTest() throws Exception {
        io.packagecloud.client.Connection connection =
                new io.packagecloud.client.Connection("localhost", 8080, "http");
        Credentials credentials = new Credentials("joe", "asdlkfjladsf8asd9f0afadsfalksjf;lasj");
        Client client = new Client(credentials, connection);
        pcloud = new PackageCloud(client);
    }

    @Test
    public void testGetDistributions() throws Exception {
        Distributions dist = pcloud.getDistributions();

        TestHttpContext ctx = TestHttpContext.getInstance();

        assertEquals(ctx.getRequest().getPath().toString(), "/api/v1/distributions.json");
        assertNotNull(ctx.getRequest().getValue("Authorization"));
        assertEquals(ctx.getResponse().getStatus(), Status.OK);
        assertNotNull(dist.deb);
        assertNotNull(dist.rpm);
        assertNotNull(dist.dsc);
    }

    @Test
    public void testPutPackage() throws Exception {

        InputStream fileStream = getClass().getResourceAsStream("/libampsharp2.0-cil_2.0.4-1_all.deb");
        byte[] bytes = IOUtils.toByteArray(fileStream);

        Package pkg = new Package("libampsharp2.0-cil_2.0.4-1_all.deb", bytes, "mystuff", 16);

        pcloud.putPackage(pkg);

        TestHttpContext ctx = TestHttpContext.getInstance();

        // Check distro ID
        String distroVersionId = ctx.getRequest().getPart("package[distro_version_id]").getContent();
        assertEquals(distroVersionId, "16");

        // Check uploadedBytes vs our bytes
        Part filePart = ctx.getRequest().getPart("package[package_file]");
        byte[] uploadedBytes = IOUtils.toByteArray(filePart.getInputStream());
        assertArrayEquals(bytes, uploadedBytes);

        assertEquals(ctx.getRequest().getPath().toString(), "/api/v1/repos/joe/mystuff/packages.json");
        assertNotNull(filePart.getFileName());
        assertEquals("application/octet-stream", filePart.getContentType().toString());
    }

    @Test
    public void testPutPackageToCollabRepo() throws Exception {

        InputStream fileStream = getClass().getResourceAsStream("/libampsharp2.0-cil_2.0.4-1_all.deb");
        byte[] bytes = IOUtils.toByteArray(fileStream);

        Package pkg = new Package("libampsharp2.0-cil_2.0.4-1_all.deb", bytes, "mystuff", 16);
        pcloud.putPackage(pkg, "julio");

        TestHttpContext ctx = TestHttpContext.getInstance();

        // Check distro ID
        String distroVersionId = ctx.getRequest().getPart("package[distro_version_id]").getContent();
        assertEquals(distroVersionId, "16");

        // Check uploadedBytes vs our bytes
        Part filePart = ctx.getRequest().getPart("package[package_file]");
        byte[] uploadedBytes = IOUtils.toByteArray(filePart.getInputStream());
        assertArrayEquals(bytes, uploadedBytes);

        assertEquals(ctx.getRequest().getPath().toString(), "/api/v1/repos/julio/mystuff/packages.json");
        assertNotNull(filePart.getFileName());
        assertEquals("application/octet-stream", filePart.getContentType().toString());
    }

    @Test
    public void testPutGemPackage() throws Exception {

        InputStream fileStream = getClass().getResourceAsStream("/chewbacca-1.0.0.gem");
        Package pkg = new Package("chewbacca-1.0.0.gem", fileStream, "mystuff");

        pcloud.putPackage(pkg);

        TestHttpContext ctx = TestHttpContext.getInstance();
        Part distroVersionId = ctx.getRequest().getPart("package[distro_version_id]");
        assertNull(distroVersionId);
    }

    @Test
    public void testPutSourcePackage() throws Exception {

        InputStream fileStream = getClass().getResourceAsStream("/natty_dsc/jake_1.0-7.dsc");

        InputStream sourceFile0 = getClass().getResourceAsStream("/natty_dsc/jake_1.0.orig.tar.bz2");
        InputStream sourceFile1 = getClass().getResourceAsStream("/natty_dsc/jake_1.0-7.debian.tar.gz");

        Map<String, InputStream> sourceFiles = new HashMap<String, InputStream>();

        sourceFiles.put("jake_1.0.orig.tar.bz2", sourceFile0);
        sourceFiles.put("jake_1.0-7.debian.tar.gz", sourceFile1);

        Package pkg = new Package("jake_1.0-7.dsc", fileStream, "mystuff", 16, sourceFiles);

        pcloud.putPackage(pkg);

        TestHttpContext ctx = TestHttpContext.getInstance();

        String distroVersionId = ctx.getRequest().getPart("package[distro_version_id]").getContent();
        assertEquals(distroVersionId, "16");

        // Reopen all the inputStreams again for testing
        fileStream = getClass().getResourceAsStream("/natty_dsc/jake_1.0-7.dsc");

        sourceFile0 = getClass().getResourceAsStream("/natty_dsc/jake_1.0.orig.tar.bz2");
        sourceFile1 = getClass().getResourceAsStream("/natty_dsc/jake_1.0-7.debian.tar.gz");

        InputStream packageFile = ctx.getRequest().getPart("package[package_file]").getInputStream();
        byte[] fileStreamBytes = IOUtils.toByteArray(fileStream);
        byte[] uploadedbytes = IOUtils.toByteArray(packageFile);
        assertArrayEquals(fileStreamBytes, uploadedbytes);

        List<Part> parts = ctx.getRequest().getParts();

        Iterator it = parts.iterator();

        //Counter to ensure we ran these specs:
        int runs = 0;

        while(it.hasNext()){
            Part part = (Part)it.next();
            String filename = part.getFileName();
            if (filename != null) {
                if(filename.equals("jake_1.0.orig.tar.bz2")) {
                    byte[] sourceFile0Bytes = IOUtils.toByteArray(part.getInputStream());
                    byte[] expectedSourceFile0Bytes = IOUtils.toByteArray(sourceFile0);
                    assertArrayEquals(expectedSourceFile0Bytes, sourceFile0Bytes);
                    runs++;
                } else if(filename.equals("jake_1.0-7.debian.tar.gz")) {
                    byte[] sourceFile1Bytes = IOUtils.toByteArray(part.getInputStream());
                    byte[] expectedSourceFile1Bytes = IOUtils.toByteArray(sourceFile1);
                    assertArrayEquals(expectedSourceFile1Bytes, sourceFile1Bytes);
                    runs++;
                }
                it.remove();
            }
        }
        assertEquals(2, runs);
    }


    @Test(expected=IllegalArgumentException.class)
    public void testPackageContentsWithNoDist() throws Exception {

        InputStream fileStream = getClass().getResourceAsStream("/natty_dsc/jake_1.0-7.dsc");
        Package pkg = new Package("jake_1.0-7.dsc", fileStream, "mystuff");

        pcloud.packageContents(pkg);
    }

    @Test
    public void testPackageContents() throws Exception {

        InputStream fileStream = getClass().getResourceAsStream("/natty_dsc/jake_1.0-7.dsc");
        Package pkg = new Package("jake_1.0-7.dsc", fileStream, "mystuff", 16);

        Contents contents = pcloud.packageContents(pkg);

        TestHttpContext ctx = TestHttpContext.getInstance();

        String distroVersionId = ctx.getRequest().getPart("package[distro_version_id]").getContent();
        assertEquals(distroVersionId, "16");

        assertEquals(contents.files.size(), 2);
        assertEquals(contents.files.get(0).filename, "jake_1.0.orig.tar.bz2");
        assertEquals(contents.files.get(0).md5sum, "a7a309b55424198ee98abcb8092d7be0");
        contents.files.get(0);
    }

}



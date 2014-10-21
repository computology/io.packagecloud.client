package io.packagecloud.client;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertNotNull;


public class PackageTest {
    @Test(expected = IllegalArgumentException.class)

    public void testPackageRaiseIfFileStreamNull() {
        ByteArrayInputStream bos = null;
        Package p = new Package(bos, "myrepo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPackageRaiseIfRepositoryNull() {
        byte[] b = {((byte)9)};
        Package p = new Package(new ByteArrayInputStream(b), null);
    }
    @Test
    public void testPackageFilestreamGetter() {
        byte[] b = {((byte)9)};
        Package p = new Package(new ByteArrayInputStream(b), "myrepo");
        assertNotNull(p.getFilestream());
    }

}

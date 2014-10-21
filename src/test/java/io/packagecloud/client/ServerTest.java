package io.packagecloud.client;

import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.KeyStore;

/*
 * This tests the fake http server
 */
public class ServerTest {

    public class HelloWorldApp implements Container {

        @Override
        public void handle(Request request, Response response) {
            try {
                PrintStream body = response.getPrintStream();
                long time = System.currentTimeMillis();

                response.setValue("Content-Type", "application/json");
                response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
                response.setDate("Date", time);
                response.setDate("Last-Modified", time);

                body.println("hi");
                body.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static String SERVER_KS_PASSWORD = "foobar";
    public static String CA_KS_PASSWORD = "foobar";

    private SSLContext initSSLContext() throws Exception {
        KeyManagerFactory km = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore serverKeystore = KeyStore.getInstance(KeyStore.getDefaultType());

        InputStream keystoreFile = getClass().getResourceAsStream("testcert.keystore");
        serverKeystore.load(keystoreFile , SERVER_KS_PASSWORD.toCharArray());

        km.init(serverKeystore, SERVER_KS_PASSWORD.toCharArray());

        TrustManagerFactory tm = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore caKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        InputStream caCertFile = getClass().getResourceAsStream("testcert.keystore");
        caKeyStore.load(caCertFile, CA_KS_PASSWORD.toCharArray());

        tm.init(caKeyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(km.getKeyManagers(), tm.getTrustManagers(), null);

        return sslContext;
    }

    @Test
    public void testServer() throws Exception {
        String shouldTest = System.getProperty("testServer");
        if (shouldTest != null && shouldTest.equals("true")){

            Container container = new HelloWorldApp();
            Server server = new ContainerServer(container);
            SSLContext sslContext = initSSLContext();
            SecureServer secureServer = new SecureServer(server);

            Connection connection = new SocketConnection(secureServer);
            SocketAddress address = new InetSocketAddress(8181);

            connection.connect(address, sslContext);
            System.out.println("starting server on 0:8181..");
            Thread.sleep(Integer.MAX_VALUE);
        }

    }

}

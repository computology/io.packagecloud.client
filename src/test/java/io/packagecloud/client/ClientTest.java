package io.packagecloud.client;

import org.apache.http.HttpHost;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {
    @Test(expected = RuntimeException.class)
    public void testRaiseIfUsernameNotFound() {
        Credentials credentials = new Credentials(null, "asd");
    }

    @Test(expected = RuntimeException.class)
    public void testRaiseIfTokenNotFound() {
        Credentials credentials = new Credentials("asdf", null);
    }

    @Test
    public void testProdDefault() {
        Credentials credentials = new Credentials("test", "asdlfkajslfjas");
        Client client = new Client(credentials);
        HttpHost host = client.getTargetHost();
        assertEquals("https://packagecloud.io:443", host.toString());
    }

}

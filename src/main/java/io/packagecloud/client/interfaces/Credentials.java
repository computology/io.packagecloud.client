package io.packagecloud.client.interfaces;

/**
 * The interface for Credentials
 */
public interface Credentials {

    /**
     * Gets token.
     *
     * @return the token
     */
    String getToken();

    /**
     * Gets username.
     *
     * @return the username
     */
    String getUsername();
}

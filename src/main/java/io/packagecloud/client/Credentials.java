package io.packagecloud.client;

import java.util.logging.Logger;

/**
 * Credentials, used to manage username/token combinations.
 */
public class Credentials implements io.packagecloud.client.interfaces.Credentials {
    private static Logger logger = LoggerProvider.getLogger();

    private final String username;
    private final String token;

    /**
     * Instantiates a new Credentials.
     *
     * @param username the username
     * @param token the token
     */
    public Credentials(String username, String token) {

        if(username == null){
            throw new RuntimeException("username cannot be null");
        }
        if(token == null){
            throw new RuntimeException("token cannot be null");
        }

        this.username = username;
        this.token = token;
        logger.info(String.format("%s/%s", username, getTokenForLogging()));
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getTokenForLogging() {
        String halfToken = token.substring(0,4);
        return halfToken + new String(new char[20]).replace("\0", "*");
    }

}

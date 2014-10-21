package io.packagecloud.client;

import java.util.logging.Logger;

/**
 * Connection, used to manage host/scheme/port configuration
 */
public class Connection {
    private static Logger logger = LoggerProvider.getLogger();

    private final String host;
    private final String scheme;
    private final Integer port;

    /**
     * Instantiates a new Connection.
     *
     * @param host the host
     * @param port the port
     * @param scheme the scheme
     */
    public Connection(String host,  Integer port, String scheme) {
        this.host = host;
        this.scheme = scheme;
        this.port = port;
        logger.info(String.format("Connecting to %s://%s:%s", scheme, host, port));
    }

    /**
     * Instantiates a new Connection.
     */
    public Connection(){
        this("packagecloud.io", 443, "https");
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets scheme.
     *
     * @return the scheme
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public Integer getPort() {
        return port;
    }
}

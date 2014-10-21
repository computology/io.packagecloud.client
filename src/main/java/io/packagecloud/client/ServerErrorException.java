package io.packagecloud.client;

public class ServerErrorException extends Exception {
    public ServerErrorException() {
    }

    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(Throwable cause) {
        super(cause);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}

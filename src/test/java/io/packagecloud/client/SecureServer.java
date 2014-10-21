package io.packagecloud.client;

import org.simpleframework.transport.Server;
import org.simpleframework.transport.Socket;

import java.io.IOException;

public class SecureServer implements Server {

    private Server delegateServer;

    public SecureServer(Server delegateServer) {
        this.delegateServer = delegateServer;
    }

    public void process(Socket socket){
//        socket.getEngine().setNeedClientAuth(true);
        try {
            delegateServer.process(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws IOException {
        delegateServer.stop();
    }
}

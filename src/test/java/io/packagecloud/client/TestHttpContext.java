package io.packagecloud.client;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public class TestHttpContext {
    private static TestHttpContext instance = null;
    protected TestHttpContext() {
        // Exists only to defeat instantiation.
    }
    public static TestHttpContext getInstance() {
        if(instance == null) {
            instance = new TestHttpContext();
        }
        return instance;
    }

    private Request request;
    private Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

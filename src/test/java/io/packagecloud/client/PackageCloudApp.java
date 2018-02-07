package io.packagecloud.client;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;

import java.io.*;
import java.util.Scanner;

/*
 * Fake packagecloud server, used for testing
 */
public class PackageCloudApp implements Container {

    private void standardHeaders(Response response) {
        long time = System.currentTimeMillis();
        response.setValue("Content-Type", "application/json");
        response.setValue("Server", "PackageCloudApp/1.0 (Simple 4.0)");
        response.setDate("Date", time);
        response.setDate("Last-Modified", time);
    }

    private void getDistributions(Request request, Response response) throws IOException {
        PrintStream body = response.getPrintStream();

        BufferedInputStream distros = new BufferedInputStream(getClass().getResourceAsStream("/distros_with_fake_distro.json"));
        int b = 0;
        standardHeaders(response);
        while ((b = distros.read()) != -1) {
            body.print((char) b);
        }
        body.close();
    }

    private void putPackage(Request request, Response response) throws IOException {
        PrintStream body = response.getPrintStream();

        standardHeaders(response);
        body.println("{}");
        body.close();
    }

    private void packageContents(Request request, Response response) throws IOException {
        PrintStream body = response.getPrintStream();

        String json = "{\"files\":[{\"filename\":\"jake_1.0.orig.tar.bz2\",\"size\":1108,\"md5sum\":\"a7a309b55424198ee98abcb8092d7be0\"},{\"filename\":\"jake_1.0-7.debian.tar.gz\",\"size\":1571,\"md5sum\":\"0fa5395e95ddf846b419e96575ce8044\"}]}";
        standardHeaders(response);
        body.println(json);
        body.close();
    }

    public void handle(Request request, Response response) {
        String path = String.valueOf(request.getPath());
        System.out.println(request.getPath().toString());
        try {
            if (path.equals("/api/v1/distributions.json")) {
                getDistributions(request, response);
            } else if (path.endsWith("packages/contents")) {
                packageContents(request, response);
            } else if (path.startsWith("/api/v1/repos")) {
                putPackage(request, response);
            } else {
                PrintStream body = response.getPrintStream();
                response.setStatus(Status.NOT_FOUND);
                body.println("not found");
                body.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            TestHttpContext cxt = TestHttpContext.getInstance();
            cxt.setRequest(request);
            cxt.setResponse(response);
        }

    }


}

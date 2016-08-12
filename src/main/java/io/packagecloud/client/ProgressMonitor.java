package io.packagecloud.client;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.OutputStream;

import io.packagecloud.client.interfaces.ProgressListener;

public class ProgressMonitor extends HttpEntityWrapper { //TODO buffered entity wrapper maybe?

    private final ProgressListener progressListener;
    private final String filename;

    public ProgressMonitor(HttpEntity build, ProgressListener progressListener, String filename) {
        super(build);
        this.progressListener = progressListener;
        this.filename = filename;
    }


    @Override
    public void writeTo(OutputStream os) throws IOException {
        super.writeTo(new ProgressOutputStream(os, progressListener, filename));
    }
}

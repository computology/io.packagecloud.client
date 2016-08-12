package io.packagecloud.client;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.packagecloud.client.interfaces.ProgressListener;

public class ProgressOutputStream extends FilterOutputStream {
    long bytes = 0;

    private final ProgressListener progressListener;
    private final String filename;

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public ProgressOutputStream(OutputStream out, ProgressListener progressListener, String filename) {
        super(out);
        this.progressListener = progressListener;
        this.filename = filename;
    }

    @Override
    public void write(int b) throws IOException {
        byte[] arr = new byte[]{(byte) b};
        write(arr, 0, arr.length);
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        bytes += len;
        progressListener.update(bytes, filename);
    }
}

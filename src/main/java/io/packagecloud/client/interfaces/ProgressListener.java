package io.packagecloud.client.interfaces;

public interface ProgressListener {
    public void update(long bytes, String filename);
}

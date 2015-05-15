package io.packagecloud.client.interfaces;

import java.io.InputStream;
import java.util.Map;

public interface Client {

    /**
     * Put package.
     *
     * @param fileStream the file stream
     * @param filename the filename
     * @param username the username
     * @param repository the repository
     * @param distroVersionId the distro version id
     * @param sourceFiles the source files
     * @return the boolean
     * @throws Exception the exception
     */
    public io.packagecloud.client.interfaces.Result putPackage(
            InputStream fileStream,
            String filename,
            String username,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles) throws Exception;

    /**
     * Gets all distributions.
     *
     * @return the distributions
     * @throws Exception the exception
     */
    public io.packagecloud.client.interfaces.Result getDistributions() throws Exception;

    /**
     * Gets the contents of a package
     *
     * @param fileStream the file stream
     * @param filename the filename
     * @param repository the repository
     * @return the string
     * @throws Exception the exception
     */
    public io.packagecloud.client.interfaces.Result packageContents(InputStream fileStream,
                                  String filename,
                                  String repository) throws Exception;


}

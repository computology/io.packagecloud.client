package io.packagecloud.client;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;
import java.io.File;

/**
 * The type Package.
 */
public class Package {
    private static Logger logger = LoggerProvider.getLogger();
    private static String[] supportedExtensions = {"deb", "dsc", "gem", "rpm", "whl", "zip", "egg", "egg-info", "tar", "bz2", "Z", "gz", "jar"};

    private final InputStream filestream;
    private final String repository;
    private final Integer distroVersionId;

    private String filename;
    private Map<String, InputStream> sourceFiles;



    /**
     * Instantiates a new Package.
     *
     * @param filestream the filestream
     * @param repository the repository
     */
    public Package(
            String filename,
            InputStream filestream,
            String repository) {
        this(filename, filestream, repository, null, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filename the filename
     * @param filestream the filestream
     * @param repository the repository
     * @param distroVersionId the distro version id
     */
    public Package(
            String filename,
            InputStream filestream,
            String repository,
            Integer distroVersionId) {
        this(filename, filestream, repository, distroVersionId, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filename the filename
     * @param bytes the bytes
     * @param repository the repository
     * @param distroVersionId the distro version id
     */
    public Package(
            String filename,
            byte[] bytes,
            String repository,
            Integer distroVersionId) {
        this(filename, new ByteArrayInputStream(bytes), repository, distroVersionId, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filename the filename
     * @param bytes the bytes
     * @param repository the repository
     */
    public Package(
            String filename,
            byte[] bytes,
            String repository) {
        this(filename, new ByteArrayInputStream(bytes), repository, null, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filename the filename
     * @param bytes the bytes
     * @param repository the repository
     * @param distroVersionId the distro version id
     * @param sourceFiles the source files
     */
    public Package(
            String filename,
            byte[] bytes,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles) {
        this(filename, new ByteArrayInputStream(bytes), repository, distroVersionId, sourceFiles);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filename the filename
     * @param filestream the filestream
     * @param repository the repository
     * @param distroVersionId the distro version id
     * @param sourceFiles the source files
     */
    public Package(
            String filename,
            InputStream filestream,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles) {

        logger.fine(String.format("new Package(%s, %s, %s, %s", filename, repository, distroVersionId, sourceFiles));

        if (filename == null) {
            throw new IllegalArgumentException("filename missing");
        }

        if (filestream == null) {
            throw new IllegalArgumentException("filestream missing");
        }

        if (repository == null) {
            throw new IllegalArgumentException("repository missing");
        }

        this.filestream = filestream;
        this.filename = filename;
        this.repository = repository;
        this.distroVersionId = distroVersionId;
        this.sourceFiles = sourceFiles;

    }

    /**
     * Get Package from a java.io.File
     * @param file the file object
     * @param repository the repository
     *
     * @return the Package
     */
    public static Package fromFile(
            File file,
            String repository) throws FileNotFoundException {
        FileInputStream filestream = new FileInputStream(file);
        if (file.isDirectory() || !file.canRead()){
            throw new IllegalArgumentException(String.format("could not open or read: %s", file.getName()));
        }
        return new Package(file.getName(), filestream, repository);
    }

    /**
     * Get Package from a java.io.File
     * @param file the file object
     * @param repository the repository
     * @param distroVersionId the distro version id
     *
     * @return the Package
     */
    public static Package fromFile(
            File file,
            String repository,
            Integer distroVersionId) throws FileNotFoundException {
        FileInputStream filestream = new FileInputStream(file);
        if (file.isDirectory() || !file.canRead()){
            throw new IllegalArgumentException(String.format("could not open or read: %s", file.getName()));
        }
        return new Package(file.getName(), filestream, repository, distroVersionId);
    }

    /**
     * Get supported extensions.
     *
     * @return the string [ ]
     */
    public static String[] getSupportedExtensions() {
        return supportedExtensions;
    }

    /**
     * Gets repository.
     *
     * @return the repository
     */
    public String getRepository() {
        return repository;
    }

    /**
     * Gets distro version id.
     *
     * @return the distro version id
     */
    public Integer getDistroVersionId() {
        return distroVersionId;
    }

    /**
     * Gets filestream.
     *
     * @return the filestream
     */
    public InputStream getFilestream() {
        return filestream;
    }

    /**
     * Gets filename.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets filename.
     *
     * @param filename the filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Gets source files.
     *
     * @return the source files
     */
    public Map<String, InputStream> getSourceFiles() {
        return sourceFiles;
    }

    /**
     * Sets source files.
     *
     * @param sourceFiles the source files
     */
    public void setSourceFiles(Map<String, InputStream> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

}

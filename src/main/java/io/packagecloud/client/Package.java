package io.packagecloud.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The type Package.
 */
public class Package {
    private static Logger logger = LoggerProvider.getLogger();
    private static String[] supportedExtensions = {"deb", "dsc", "gem", "rpm"};

    private final SecureRandom random = new SecureRandom();
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
            InputStream filestream,
            String repository) {
        this(filestream, repository, null, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filestream the filestream
     * @param repository the repository
     * @param distroVersionId the distro version id
     */
    public Package(
            InputStream filestream,
            String repository,
            Integer distroVersionId) {
        this(filestream, repository, distroVersionId, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param bytes the bytes
     * @param repository the repository
     * @param distroVersionId the distro version id
     */
    public Package(
            byte[] bytes,
            String repository,
            Integer distroVersionId) {
        this(new ByteArrayInputStream(bytes), repository, distroVersionId, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param bytes the bytes
     * @param repository the repository
     */
    public Package(
            byte[] bytes,
            String repository) {
        this(new ByteArrayInputStream(bytes), repository, null, null);
    }

    /**
     * Instantiates a new Package.
     *
     * @param bytes the bytes
     * @param repository the repository
     * @param distroVersionId the distro version id
     * @param sourceFiles the source files
     */
    public Package(
            byte[] bytes,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles) {
        this(new ByteArrayInputStream(bytes), repository, distroVersionId, sourceFiles);
    }

    /**
     * Instantiates a new Package.
     *
     * @param filestream the filestream
     * @param repository the repository
     * @param distroVersionId the distro version id
     * @param sourceFiles the source files
     */
    public Package(
            InputStream filestream,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles) {

        logger.fine(String.format("new Package(%s, %s, %s, %s", filestream, repository, distroVersionId, sourceFiles));

        if (filestream == null) {
            throw new IllegalArgumentException("filestream missing");
        }

        if (repository == null) {
            throw new IllegalArgumentException("repository missing");
        }

        this.filestream = filestream;
        this.filename = generateFilename();
        this.repository = repository;
        this.distroVersionId = distroVersionId;
        this.sourceFiles = sourceFiles;

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

    /**
     * Generate filename.
     *
     * @return the string
     */
    public String generateFilename() {
        return new BigInteger(130, random).toString(32);
    }
}

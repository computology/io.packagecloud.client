package io.packagecloud.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.packagecloud.client.interfaces.*;

import java.io.IOException;

/**
 * The main PackageCloud class, delegates calls to a configured Client
 */
public class PackageCloud {

    private final Client client;
    private final ObjectMapper mapper;

    public Client getClient() {
        return client;
    }

    /**
     * Instantiates a new PackageCloud object
     *
     * @param configuredClient a configured Client
     * @see Client
     */
    public PackageCloud(Client configuredClient) {
        this.client = configuredClient;
        this.mapper = new ObjectMapper();
    }

    /**
     * Gets available distributions, see
     * the <a href="https://packagecloud.io/docs/api#resource_distributions_method_index">distributions/index</a>
     * api docs.
     *
     * @return Parsed contents of /api/v1/distributions
     * @throws io.packagecloud.client.UnauthorizedException
     * @throws java.io.IOException
     * @throws io.packagecloud.client.ServerErrorException
     *
     *
     */
    public Distributions getDistributions() throws UnauthorizedException, IOException, ServerErrorException {
        io.packagecloud.client.interfaces.Result result = client.getDistributions();
        return mapper.readValue(result.getResponse(), Distributions.class);
    }

    /**
     * Put package.
     *
     * @param pkg the Package to upload
     * @return the boolean
     * @throws io.packagecloud.client.UnauthorizedException
     * @throws java.io.IOException
     * @throws io.packagecloud.client.ServerErrorException
     */
    public boolean putPackage(Package pkg) throws UnauthorizedException, IOException, ServerErrorException {
        client.putPackage(
                pkg.getFilestream(),
                pkg.getFilename(),
                pkg.getRepository(),
                pkg.getDistroVersionId(),
                pkg.getSourceFiles());

        return true;
    }

    /**
     * Put package to a shared repository.
     *
     * @param pkg the Package to upload
     * @param username the username of owner of the shared repository
     * @return the boolean
     * @throws io.packagecloud.client.UnauthorizedException
     * @throws java.io.IOException
     * @throws io.packagecloud.client.ServerErrorException
     */
    public boolean putPackage(Package pkg, String username) throws UnauthorizedException, IOException, ServerErrorException {
        client.putPackage(
                pkg.getFilestream(),
                pkg.getFilename(),
                username,
                pkg.getRepository(),
                pkg.getDistroVersionId(),
                pkg.getSourceFiles());

        return true;
    }

    /**
     * Package contents.
     *
     * @param pkg the pkg
     * @return the contents
     * @throws io.packagecloud.client.UnauthorizedException
     * @throws java.io.IOException
     * @throws io.packagecloud.client.ServerErrorException
     */
    public Contents packageContents(Package pkg) throws Exception {
        Result result = client.packageContents(pkg.getFilestream(), pkg.getFilename(), pkg.getRepository());
        return mapper.readValue(result.getResponse(), Contents.class);
    }



}

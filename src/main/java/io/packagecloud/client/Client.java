package io.packagecloud.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.logging.Logger;

import io.packagecloud.client.interfaces.ProgressListener;

public class Client implements io.packagecloud.client.interfaces.Client {

    private static Logger logger = LoggerProvider.getLogger();

    private final HttpHost targetHost;
    private final CloseableHttpClient httpClient = getConfiguredHttpClient();
    private ProgressListener progressListener;

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    private CloseableHttpClient getConfiguredHttpClient() {
        return HttpClients
                .custom()
                .setUserAgent("io.packagecloud.client 2.0.3")
                .build();
    }

    private final HttpClientContext context = HttpClientContext.create();
    private final SecureRandom random = new SecureRandom();
    private final Credentials credentials;

    public Client(Credentials credentials) {
        this(credentials, new Connection());
    }

    public Client(Credentials credentials, Connection connection) {
        this.credentials = credentials;

        this.targetHost = new HttpHost(
                connection.getHost(),
                connection.getPort(),
                connection.getScheme());

        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, "basic"),
                new UsernamePasswordCredentials(getToken(), ""));
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
    }


    public HttpHost getTargetHost() {
        return targetHost;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public String getToken() {
        return this.getCredentials().getToken();
    }
    public String getUsername() {
        return this.getCredentials().getUsername();
    }

    @Override
    public Result getDistributions()
            throws UnauthorizedException, IOException, ServerErrorException {
        String strResponse;
        StatusLine statusLine;

        HttpGet httpGet = new HttpGet("/api/v1/distributions.json");

        CloseableHttpResponse response = httpClient.execute(
                targetHost, httpGet, context);
        try {
            statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            strResponse = EntityUtils.toString(entity);
            logger.info(response.getStatusLine().toString());
        } finally {
            response.close();
        }

        return getResultOrThrow(strResponse, statusLine);
    }

    @Override
    public Result packageContents(InputStream fileStream, String filename, String repository, Integer distroVersionId)
            throws UnauthorizedException, IOException, ServerErrorException {
        String strResponse;
        StatusLine statusLine;

        String url = String.format("/api/v1/repos/%s/%s/packages/contents", getUsername(), repository);

        HttpPost httppost = new HttpPost(url);

        ByteArrayBody body = bodyFromInputStream(fileStream, filename);

        StringBody distro = new StringBody(String.valueOf(distroVersionId), ContentType.TEXT_PLAIN);

        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("package[package_file]", body)
                .addPart("package[distro_version_id]", distro)
                .build();

        httppost.setEntity(reqEntity);
        CloseableHttpResponse response = httpClient.execute(
                targetHost, httppost, context);

        try {
            statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            strResponse = EntityUtils.toString(entity);
            logger.info(response.getStatusLine().toString());
        } finally {
            response.close();
        }

        return getResultOrThrow(strResponse, statusLine);
    }


    public Result putPackage(
            InputStream fileStream,
            String filename,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles)
            throws UnauthorizedException, IOException, ServerErrorException {
        return putPackage(fileStream, filename, getUsername(), repository, distroVersionId, sourceFiles);
    }

    @Override
    public Result putPackage(
            InputStream fileStream,
            String filename,
            String username,
            String repository,
            Integer distroVersionId,
            Map<String, InputStream> sourceFiles)
    throws UnauthorizedException, IOException, ServerErrorException {
        String strResponse;
        StatusLine statusLine;

        String url = String.format("/api/v1/repos/%s/%s/packages.json", username, repository);

        HttpPost httppost = new HttpPost(url);

        ByteArrayBody body = bodyFromInputStream(fileStream, generateFilename());

        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create()
                .addPart("package[package_file]", body);

        if (distroVersionId != null) {
            StringBody distro = new StringBody(String.valueOf(distroVersionId), ContentType.TEXT_PLAIN);
            reqEntity.addPart("package[distro_version_id]", distro);
        }

        if (sourceFiles != null) {
            for (String key : sourceFiles.keySet()) {
                logger.fine(String.format("Processing SourceFile part: %s", key));
                InputStream sourceStream = sourceFiles.get(key);
                ByteArrayBody sourceFileBody = bodyFromInputStream(sourceStream, key);
                reqEntity.addPart("package[source_files][]", sourceFileBody);
            }
        }

        if (this.progressListener != null) {
            httppost.setEntity(new ProgressMonitor(reqEntity.build(), this.progressListener, filename));
        } else {
            httppost.setEntity(reqEntity.build());

        }

        CloseableHttpResponse response = httpClient.execute(
                targetHost, httppost, context);

        try {
            statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            strResponse = EntityUtils.toString(entity);
            logger.info(response.getStatusLine().toString());
        } finally {
            response.close();
        }

        return getResultOrThrow(strResponse, statusLine);
    }

    private Result getResultOrThrow(String strResponse, StatusLine statusLine)
            throws UnauthorizedException, ServerErrorException {

        if (statusLine != null && (statusLine.getStatusCode() == 200 || statusLine.getStatusCode() == 201)){
            return new Result(strResponse);
        } else if(statusLine != null && statusLine.getStatusCode() == 401) {
            throw new UnauthorizedException();
        } else {
            String exception = String.format("%s:\n%s", statusLine, strResponse);
            throw new ServerErrorException(exception);
        }

    }


    //TODO: we can probably replace this with InputStreamBody
    private ByteArrayBody bodyFromInputStream(InputStream is, String filename) throws IOException {
        byte[] bytes = IOUtils.toByteArray(is);
        return new ByteArrayBody(bytes, filename);
    }

    private String generateFilename() {
        return new BigInteger(130, random).toString(32);
    }

}

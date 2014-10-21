package io.packagecloud.client;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON class for Version items inside of Distribution
 * @see io.packagecloud.client.Distribution
 */
public class Version {
    @JsonProperty("id")
    public int id;

    @JsonProperty("display_name")
    public String displayName;

    @JsonProperty("version_number")
    public String versionNumber;

    @JsonProperty("index_name")
    public String indexName;
}

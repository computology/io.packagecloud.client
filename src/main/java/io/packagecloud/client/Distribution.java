package io.packagecloud.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * JSON class for Distribution items in Distributions response
 * @see io.packagecloud.client.Distributions
 */
public class Distribution {
    @JsonProperty("display_name")
    public String displayName;

    @JsonProperty("index_name")
    public String indexName;

    @JsonProperty("versions")
    public List<Version> versions;
}

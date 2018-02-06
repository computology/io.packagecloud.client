package io.packagecloud.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * JSON class for Distributions Response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Distributions {
    public List<Distribution> deb;
    public List<Distribution> dsc;
    public List<Distribution> rpm;
    public List<Distribution> py;
    public List<Distribution> node;
    public List<Distribution> jar;
}

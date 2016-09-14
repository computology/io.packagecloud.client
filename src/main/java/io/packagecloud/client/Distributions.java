package io.packagecloud.client;

import java.util.List;

/**
 * JSON class for Distributions Response
 */
public class Distributions {
    public List<Distribution> deb;
    public List<Distribution> dsc;
    public List<Distribution> rpm;
    public List<Distribution> py;
    public List<Distribution> jar;
}

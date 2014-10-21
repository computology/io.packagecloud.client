package io.packagecloud.client;

public class Result implements io.packagecloud.client.interfaces.Result {

    private final String response;

    public Result(String response){
        this.response = response;
    }

    @Override
    public String getResponse() {
        return response;
    }
}

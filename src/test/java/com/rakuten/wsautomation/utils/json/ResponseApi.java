package com.rakuten.wsautomation.utils.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseApi {

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private String token;

}

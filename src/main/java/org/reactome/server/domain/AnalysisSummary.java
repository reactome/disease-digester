package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisSummary {
    //    just need the token info only
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AnalysisSummary{" +
                "token='" + token + '\'' +
                '}';
    }
}

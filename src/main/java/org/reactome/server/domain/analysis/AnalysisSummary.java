package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisSummary {
    /*this object is used for deserialize the returned json object from restemplate */
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
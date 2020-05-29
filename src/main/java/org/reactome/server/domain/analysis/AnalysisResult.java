package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
//public class AnalysisResult extends ResponseEntity<AnalysisParameter> {
public class AnalysisResult  {
    /*this object is used for serializing the disease analysis returned json file*/
    private HttpStatus status;
    private String url = null;
    private String error = null;
    private AnalysisParameter parameter;

    public AnalysisResult(HttpStatus status, AnalysisParameter parameter) {
        this.status = status;
        this.parameter = parameter;
    }

    public int getStatus() {
        return status.value();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public AnalysisParameter getParameter() {
        return parameter;
    }

    public void setParameter(AnalysisParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "httpStatus=" + status +
                ", urlResult='" + url + '\'' +
                ", errorMessage='" + error + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}

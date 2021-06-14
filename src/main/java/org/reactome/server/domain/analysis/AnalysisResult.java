package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
//public class AnalysisResult extends ResponseEntity<AnalysisParameter> {
public class AnalysisResult {
    /*this object is used for serializing the disease analysis returned json file*/
    private HttpStatus code;
    private String url = null;
    private String message = null;
    private AnalysisParameter parameter;

    public AnalysisResult(HttpStatus status, AnalysisParameter parameter) {
        this.code = status;
        this.parameter = parameter;
    }

    public int getCode() {
        return code.value();
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                "code=" + code +
                ", url='" + url + '\'' +
                ", message='" + message + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}

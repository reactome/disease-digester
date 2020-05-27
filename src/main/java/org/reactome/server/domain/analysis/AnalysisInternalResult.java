package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonClassDescription("AnalysisResults")
public class AnalysisInternalResult {
    /*this object is used for deserialize the returned json object from restemplate */
    private AnalysisSummary summary;

    public AnalysisSummary getSummary() {
        return summary;
    }

    public void setSummary(AnalysisSummary summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "AnalysisResults{" +
                "summary=" + summary +
                '}';
    }
}
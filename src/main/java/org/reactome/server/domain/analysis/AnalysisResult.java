package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisResult {
    //  analysis token was included in the summary object
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
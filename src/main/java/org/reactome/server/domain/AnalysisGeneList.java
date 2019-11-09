package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisGeneList {
    private String analysisParameter;
    private List<String> genes;

    public String getAnalysisParameter() {
        return analysisParameter;
    }

    public void setAnalysisParameter(String analysisParameter) {
        this.analysisParameter = analysisParameter;
    }

    public List<String> getGenes() {
        return genes;
    }

    public void setGenes(List<String> genes) {
        this.genes = genes;
    }

    @Override
    public String toString() {
        return "AnalysisGeneList{" +
                "analyzeParameter='" + analysisParameter + '\'' +
                ", genes=" + genes +
                '}';
    }
}

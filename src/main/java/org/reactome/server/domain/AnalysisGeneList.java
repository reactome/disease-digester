package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisGeneList {
    private List<String> genes;

    public List<String> getGenes() {
        return genes;
    }

    public void setGenes(List<String> genes) {
        this.genes = genes;
    }

    @Override
    public String toString() {
        return "AnalysisGeneList{" +
                "genes=" + genes +
                '}';
    }
}

package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisGeneList {
    private boolean projectToHuman;
    private boolean includeInteractors;
    private List<String> genes;

    public Boolean isProjectToHuman() {
        return projectToHuman;
    }

    public void setProjectToHuman(Boolean projectToHuman) {
        this.projectToHuman = projectToHuman;
    }

    public Boolean isIncludeInteractors() {
        return includeInteractors;
    }

    public void setIncludeInteractors(Boolean includeInteractors) {
        this.includeInteractors = includeInteractors;
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
                "projectionToHuman=" + projectToHuman +
                ", includeInteractors=" + includeInteractors +
                ", genes=" + genes +
                '}';
    }
}

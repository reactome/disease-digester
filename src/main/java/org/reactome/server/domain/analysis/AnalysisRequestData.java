package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisRequestData {
    private Boolean includeInteractors;
    private Boolean redirectToReacFoam;
    private List<String> genes;

    public Boolean isIncludeInteractors() {
        return includeInteractors;
    }

    public void setIncludeInteractors(boolean includeInteractors) {
        this.includeInteractors = includeInteractors;
    }

    public Boolean isRedirectToReacfoam() {
        return redirectToReacFoam;
    }

    public void setRedirectToReacFoam(boolean redirectToReacFoam) {
        this.redirectToReacFoam = redirectToReacFoam;
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
                ", includeInteractors=" + includeInteractors +
                ", redirectToReacfoam=" + redirectToReacFoam +
                ", genes=" + genes +
                '}';
    }
}

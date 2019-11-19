package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisRequestData {
    private Boolean projectToHuman;
    private Boolean includeInteractors;
    private Boolean redirectToReacFoam;
    private List<String> genes;

    public Boolean isProjectToHuman() {
        return projectToHuman;
    }

    public void setProjectToHuman(boolean projectToHuman) {
        this.projectToHuman = projectToHuman;
    }

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
                "projectToHuman=" + projectToHuman +
                ", includeInteractors=" + includeInteractors +
                ", redirectToReacfoam=" + redirectToReacFoam +
                ", genes=" + genes +
                '}';
    }
}

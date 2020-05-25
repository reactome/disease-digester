package org.reactome.server.domain.analysis;

public class ReacfoamParameter {
    private Integer species = 48887;
    // TODO: 2020/5/25 use enum class Color
    private String color = "COPPER";
    private String analysis = null;
    private Resource resource = Resource.TOTAL;

    public ReacfoamParameter() {
    }

    public Integer getSpecies() {
        return species;
    }

    public void setSpecies(Integer species) {
        this.species = species;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getParameter() {
        assert analysis != null;
        return "/?species=" + species +
                "&color=" + color +
                "&analysis=" + analysis +
                "&resource=" + resource.getResource();
    }

    @Override
    public String toString() {
        return "ReacfoamParameter{" +
                "species=" + species +
                ", color='" + color +
                ", analysis='" + analysis +
                ", resource=" + resource.getResource() +
                '}';
    }
}

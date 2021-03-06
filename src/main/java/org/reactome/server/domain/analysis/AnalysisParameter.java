package org.reactome.server.domain.analysis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.reactome.server.service.OrderBy;

public class AnalysisParameter {
    private String disease;
    //Analyse the identifiers in the file over the different species and projects the result to Homo Sapiens
    private boolean projection = true;
    //Include interactors
    private boolean interactors = false;
    //how to sort the result
    @JsonIgnore
    private SortBy sortBy = SortBy.ENTITIES_PVALUE;
    //specifies the order
    @JsonIgnore
    private OrderBy order = OrderBy.ASC;
    //the resource to sort
    @JsonIgnore
    private Resource resource = Resource.TOTAL;
    //defines the pValue threshold. Only hit pathway with pValue equals or below the threshold will be returned
    private float pValue = 1;
    //set to ‘false’ to exclude the disease pathways from the result (it does not alter the statistics)
    private boolean includeDiseasePathways = true;

    public AnalysisParameter() {
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public boolean isProjection() {
        return projection;
    }

    public void setProjection(boolean projection) {
        this.projection = projection;
    }

    public boolean isInteractors() {
        return interactors;
    }

    public void setInteractors(boolean interactors) {
        this.interactors = interactors;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public OrderBy getOrder() {
        return order;
    }

    public void setOrder(OrderBy order) {
        this.order = order;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public float getpValue() {
        return pValue;
    }

    public void setpValue(float pValue) {
        this.pValue = pValue;
    }

    public boolean isIncludeDiseasePathways() {
        return includeDiseasePathways;
    }

    public void setIncludeDiseasePathways(boolean includeDiseasePathways) {
        this.includeDiseasePathways = includeDiseasePathways;
    }

    @JsonIgnore
    public String getParameter() {
        return projection ? "/projection" : "/" +
                "?interactors=" + interactors +
                "&sortBy=" + sortBy.getSort() +
                "&order=" + order.getOrder() +
                "&resource=" + resource.getResource() +
                "&pValue=" + pValue +
                "&includeDisease=" + includeDiseasePathways;
    }

    @Override
    public String toString() {
        return "AnalysisParameter{" +
                "disease='" + disease + '\'' +
                ", projection=" + projection +
                ", interactors=" + interactors +
                ", sortBy=" + sortBy +
                ", order=" + order +
                ", resource=" + resource +
                ", pValue=" + pValue +
                ", includeDiseasePathways=" + includeDiseasePathways +
                '}';
    }
}
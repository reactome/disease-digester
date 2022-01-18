package org.reactome.server.domain;

import org.reactome.server.domain.model.Disease;

import java.util.List;

public class DiseaseResult {
    private List<Disease> diseases;
    private Integer pageNumber;
    private Integer totalPage;

    public DiseaseResult() {
    }

    public DiseaseResult(List<Disease> diseases, Integer pageNumber, Integer totalPage) {
        this.setDiseases(diseases);
        this.pageNumber = pageNumber;
        this.totalPage = totalPage;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> content) {
        this.diseases = content;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "DiseaseResult{" +
                "content=" + diseases +
                ", pageNumber=" + pageNumber +
                ", totalPage=" + totalPage +
                '}';
    }
}

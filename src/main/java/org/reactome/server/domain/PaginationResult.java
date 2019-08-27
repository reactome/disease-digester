package org.reactome.server.domain;

import java.util.List;

public class PaginationResult {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalCount;
    private List<DiseaseItem> diseaseItems;

    public PaginationResult(Integer pageNumber, Integer pageSize, Long totalCount, List<DiseaseItem> diseaseItems) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.diseaseItems = diseaseItems;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    public void setDiseaseItems(List<DiseaseItem> diseaseItems) {
        this.diseaseItems = diseaseItems;
    }

    @Override
    public String toString() {
        return "PaginationResult{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", diseaseItems=" + diseaseItems +
                '}';
    }
}

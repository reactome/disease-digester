package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResult {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalCount;
    private String sortBy;
    private String orderBy;
    private List<DiseaseItem> diseaseItems;

    public PaginationResult(Integer pageNumber, Integer pageSize, Long totalCount, List<DiseaseItem> diseaseItems) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.diseaseItems = diseaseItems;
    }

    public PaginationResult(Integer pageNumber, Integer pageSize, Long totalCount, String sortBy, String orderBy, List<DiseaseItem> diseaseItems) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.sortBy = sortBy;
        this.orderBy = orderBy;
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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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
                ", sortBy='" + sortBy + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", diseaseItems=" + diseaseItems +
                '}';
    }
}

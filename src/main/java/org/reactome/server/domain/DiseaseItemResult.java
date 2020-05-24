package org.reactome.server.domain;

import org.reactome.server.service.OrderBy;
import org.reactome.server.service.SortBy;
import org.reactome.server.util.DiseaseItemComparator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiseaseItemResult {
    private List<DiseaseItem> diseases;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;

    public DiseaseItemResult() {
    }

    public DiseaseItemResult(List<DiseaseItem> diseases, Integer pageNumber, Integer pageSize, Integer totalPage, SortBy sortBy, OrderBy orderBy) {
        this.setDiseases(diseases, sortBy, orderBy);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
    }

    public List<DiseaseItem> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<DiseaseItem> content, SortBy sortBy, OrderBy orderBy) {
        Stream<DiseaseItem> diseaseItemStream = content.stream();
        if (sortBy == SortBy.GENE) {
            if (orderBy == OrderBy.ASC) {
                this.diseases = diseaseItemStream.sorted(DiseaseItemComparator.GeneSizASCComparator).collect(Collectors.toList());
            } else {
                this.diseases = diseaseItemStream.sorted(DiseaseItemComparator.GeneSizDESCComparator).collect(Collectors.toList());
            }
        } else {
            if (orderBy == OrderBy.ASC) {
                this.diseases = diseaseItemStream.sorted(DiseaseItemComparator.DiseaseNameASCComparator).collect(Collectors.toList());
            } else {
                this.diseases = diseaseItemStream.sorted(DiseaseItemComparator.DiseaseNameDESCComparator).collect(Collectors.toList());
            }
        }
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

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "DiseaseItemResult{" +
                "content=" + diseases +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                '}';
    }
}

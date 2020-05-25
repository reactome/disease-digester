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
    private Integer totalPage;

    public DiseaseItemResult() {
    }

    public DiseaseItemResult(List<DiseaseItem> diseases, Integer pageNumber, Integer totalPage, SortBy sortBy, OrderBy orderBy) {
        this.setDiseases(diseases, sortBy, orderBy);
        this.pageNumber = pageNumber;
        this.totalPage = totalPage;
    }

    public List<DiseaseItem> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<DiseaseItem> content, SortBy sort, OrderBy order) {
        /*need to sort disease items since the service method used two query to find all the entry:
        one to find the disease id first(get results by using a GROUP BY function), and another to find the associated gene items for that id.
        but the second query can not sort the results by geneSize( which need a group by aggregation function, and therefore go back to the circle for loop callback of GROUP BY)>*/
        // TODO: 2020/5/25 this is a dirty way to sort DiseaseItem is quite, should move the item sort part into sql query
        Stream<DiseaseItem> diseaseItemStream = content.stream();
        if (sort == SortBy.GENE) {
            if (order == OrderBy.ASC) {
                this.diseases = diseaseItemStream.sorted(DiseaseItemComparator.GeneSizASCComparator).collect(Collectors.toList());
            } else {
                this.diseases = diseaseItemStream.sorted(DiseaseItemComparator.GeneSizDESCComparator).collect(Collectors.toList());
            }
        } else {
            if (order == OrderBy.ASC) {
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
                ", totalPage=" + totalPage +
                '}';
    }
}

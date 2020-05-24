package org.reactome.server.service;

import org.reactome.server.domain.DiseaseItemResult;

import java.util.List;

public interface DiseaseItemService {
    DiseaseItemResult findAll(Integer pageNumber, Integer pageSize, Float score, Integer geneSize, SortBy sortBy, OrderBy order);

    DiseaseItemResult findByDiseaseName(Integer pageNumber, Integer pageSize, String diseaseName, Float score, Integer geneSize, SortBy sortBy, OrderBy order);

    Integer getMaxGeneSize(Float score, String diseaseName);

    List<String> getGeneListByDiseaseId(String diseaseId);
}
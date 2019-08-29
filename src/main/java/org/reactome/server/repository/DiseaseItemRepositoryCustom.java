package org.reactome.server.repository;

import org.reactome.server.domain.PaginationResult;

public interface DiseaseItemRepositoryCustom {
    PaginationResult getPaginationResult(Integer pageNumber, Integer pageSize, String sortBy, String orderBy);

    PaginationResult getPaginationResultByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, String sortBy, String orderBy);

    PaginationResult getPaginationResultByDiseaseClass(String diseaseClass, Integer pageNumber, Integer pageSize, String sortBy, String orderBy);
}
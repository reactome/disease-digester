package org.reactome.server.service;

import org.reactome.server.domain.PaginationResult;
import org.reactome.server.repository.DiseaseItemRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiseaseItemService {
    private final DiseaseItemRepositoryCustom repositoryCustom;

    @Autowired
    public DiseaseItemService(DiseaseItemRepositoryCustom repositoryCustom) {
        this.repositoryCustom = repositoryCustom;
    }

    public PaginationResult findAll(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        return repositoryCustom.getPaginationResult(pageNumber, pageSize, sortBy, orderBy);
    }

    public PaginationResult findDiseaseItemsByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        /* filtering malicious query */
        diseaseName = diseaseName.replaceAll("[^\\w]+", "");
        if (null == pageNumber) pageNumber = 1;
        if (null == pageSize) pageSize = 50;
        return repositoryCustom.getPaginationResultByDiseaseName(diseaseName, pageNumber, pageSize, sortBy, orderBy);
    }


    public PaginationResult findDiseaseItemsByDiseaseClass(String diseaseClass, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        /* filtering malicious query */
        diseaseClass = diseaseClass.replaceAll("[^\\w]+", "");
        if (null == pageNumber) pageNumber = 1;
        if (null == pageSize) pageSize = 50;
        return repositoryCustom.getPaginationResultByDiseaseClass(diseaseClass, pageNumber, pageSize, sortBy, orderBy);
    }
}

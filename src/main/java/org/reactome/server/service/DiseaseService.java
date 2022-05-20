package org.reactome.server.service;

import org.reactome.server.domain.DiseaseResult;
import org.reactome.server.domain.model.SourceDatabase;
import org.reactome.server.repository.out.GeneToDiseases;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface DiseaseService {
    @Transactional(readOnly = true)
    DiseaseResult findAll(Integer pageNumber, Integer pageSize, Float score, Integer geneSize, SourceDatabase source, SortBy sortBy, Sort.Direction order);

    @Transactional(readOnly = true)
    DiseaseResult findByDiseaseName(Integer pageNumber, Integer pageSize, String diseaseName, Float score, Integer geneSize, SourceDatabase source, SortBy sortBy, Sort.Direction order);

    Long getMaxGeneSize(Float score, String diseaseName, SourceDatabase source);

    List<String> getGeneListByDiseaseId(String diseaseId, SourceDatabase source);

    List<GeneToDiseases> findByGenes(Set<String> geneAcs, SourceDatabase source);
}
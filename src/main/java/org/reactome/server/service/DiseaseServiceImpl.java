package org.reactome.server.service;

import org.reactome.server.domain.DiseaseResult;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.SourceDatabase;
import org.reactome.server.repository.DiseaseRepository;
import org.reactome.server.repository.out.GeneToDiseases;
import org.reactome.server.repository.out.Sorted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Byron
 */
@Service
public class DiseaseServiceImpl implements DiseaseService {
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DiseaseServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DiseaseResult findAll(Integer pageNumber, Integer pageSize, Float score, Integer geneSize, SourceDatabase source, SortBy sortBy, Sort.Direction order) {
        Sort sort = Sort.by(order, sortBy == SortBy.GENE ? "associatedGenesCount" : "diseaseName")
                .and(Sort.by(order, sortBy != SortBy.GENE ? "associatedGenesCount" : "diseaseName"));

        Page<Sorted<Disease>> sortedDiseases = diseaseRepository.findAll(score, geneSize.longValue(), source, PageRequest.of(pageNumber - 1, pageSize, sort));
        List<Disease> diseases = sortedDiseases.stream().map(Sorted::getResult).collect(Collectors.toList());
        return new DiseaseResult(diseases, pageNumber, sortedDiseases.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public DiseaseResult findByDiseaseName(Integer pageNumber, Integer pageSize, String diseaseName, Float score, Integer geneSize, SourceDatabase source, SortBy sortBy, Sort.Direction order) {
        Sort sort = Sort.by(order, sortBy == SortBy.GENE ? "associatedGenesCount" : "diseaseName")
                .and(Sort.by(order, sortBy != SortBy.GENE ? "associatedGenesCount" : "diseaseName"));

        Page<Sorted<Disease>> sortedDiseases = diseaseRepository.findByDiseaseName(diseaseName, score, geneSize.longValue(), source, PageRequest.of(pageNumber - 1, pageSize, sort));
        List<Disease> diseases = sortedDiseases.stream().map(Sorted::getResult).collect(Collectors.toList());
        return new DiseaseResult(diseases, pageNumber, sortedDiseases.getTotalPages());
    }

    public Long getMaxGeneSize(Float score, String diseaseName, SourceDatabase source) {
        if (null == diseaseName) {
            return diseaseRepository.selectMaxGeneSizeByScore(score, source);
        } else {
            return diseaseRepository.selectMaxGeneSizeByScoreAndDiseaseName(diseaseName, score, source);
        }
    }

    public List<String> getGeneListByDiseaseId(String diseaseId, SourceDatabase source) {
        return diseaseRepository.getGeneListByDiseaseId(diseaseId, source);
    }

    @Override
    public List<GeneToDiseases> findByGenes(Set<String> geneAcs, SourceDatabase source) {
        return this.diseaseRepository.findByGenes(geneAcs, source)
                .stream().map(tuple -> new GeneToDiseases(tuple.get(0, String.class), tuple.get(1, BigInteger.class), tuple.get(2, String.class)))
                .collect(Collectors.toList());
    }
}
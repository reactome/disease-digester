package org.reactome.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactome.server.domain.DiseaseResult;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.repository.DiseaseRepository;
import org.reactome.server.repository.out.GeneToDiseases;
import org.reactome.server.repository.out.Interactor;
import org.reactome.server.repository.out.Sorted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.Arrays;
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
    public DiseaseResult findAll(Integer pageNumber, Integer pageSize, Float score, Integer geneSize, SortBy sortBy, Sort.Direction order) {
        Sort sort = Sort.by(order, sortBy == SortBy.GENE ? "associatedGenesCount" : "diseaseName")
                .and(Sort.by(order, sortBy != SortBy.GENE ? "associatedGenesCount" : "diseaseName"));

        Page<Sorted<Disease>> sortedDiseases = diseaseRepository.findAll(score, geneSize.longValue(), PageRequest.of(pageNumber - 1, pageSize, sort));
        List<Disease> diseases = sortedDiseases.stream().map(Sorted::getResult).collect(Collectors.toList());
        return new DiseaseResult(diseases, pageNumber, sortedDiseases.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public DiseaseResult findByDiseaseName(Integer pageNumber, Integer pageSize, String diseaseName, Float score, Integer geneSize, SortBy sortBy, Sort.Direction order) {
        Sort sort = Sort.by(order, sortBy == SortBy.GENE ? "associatedGenesCount" : "diseaseName")
                .and(Sort.by(order, sortBy != SortBy.GENE ? "associatedGenesCount" : "diseaseName"));

        Page<Sorted<Disease>> sortedDiseases = diseaseRepository.findByDiseaseName(diseaseName, score, geneSize.longValue(), PageRequest.of(pageNumber - 1, pageSize, sort));
        List<Disease> diseases = sortedDiseases.stream().map(Sorted::getResult).collect(Collectors.toList());
        return new DiseaseResult(diseases, pageNumber, sortedDiseases.getTotalPages());
    }

    public Long getMaxGeneSize(Float score, String diseaseName) {
        if (null == diseaseName) {
            return diseaseRepository.selectMaxGeneSizeByScore(score);
        } else {
            return diseaseRepository.selectMaxGeneSizeByScoreAndDiseaseName(diseaseName, score);
        }
    }

    public List<String> getGeneListByDiseaseId(String diseaseId) {
        return diseaseRepository.getGeneListByDiseaseId(diseaseId);
    }

    @Override
    public List<GeneToDiseases> findByGenes(Set<String> geneAcs) {
        return this.diseaseRepository.findByGenes(geneAcs)
                .stream().map(tuple -> new GeneToDiseases(tuple.get(0, String.class), tuple.get(1, BigInteger.class), tuple.get(2, String.class)))
                .collect(Collectors.toList());
    }
}
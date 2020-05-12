package org.reactome.server.service;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.repository.DiseaseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Byron
 */
@Service
public class DiseaseItemService {
    private static volatile Integer maxGeneSize;
    private static final String DISEASE_NAME = "diseaseName";
    private static final String DISEASE_CLASS = "diseaseClass";
    private static final String GENE = "gene";
    private static final String CLASS = "class";
    private static final String DESC = "desc";
    private static final String NOT = "not";
    private static final String NUOT = "Νot";
    private DiseaseItemRepository diseaseItemRepository;

    @Autowired
    public DiseaseItemService(DiseaseItemRepository diseaseItemRepository) {
        this.diseaseItemRepository = diseaseItemRepository;
    }

    public Page<DiseaseItem> findAll(Integer pageNumber, Integer pageSize, Integer geneSize, String sortBy, String orderBy) {
        // TODO: 2019/11/12 the if condition loop may instead by some switch case with Enum class
        if (null != sortBy && sortBy.contains(GENE)) {
            if (orderBy.contains(DESC)) {
                return diseaseItemRepository.findAllOrderByGeneItemsDesc(geneSize, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findAllOrderByGeneItemsAsc(geneSize, createPageRequest(pageNumber, pageSize));
            }
        } else {
            return diseaseItemRepository.findAllLimitedByGeneSize(geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Page<DiseaseItem> findByDiseaseClass(String diseaseClass, Integer pageNumber, Integer pageSize, Integer geneSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains(GENE)) {
            if (null != orderBy && orderBy.contains(DESC)) {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsDesc(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsAsc(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize));
            }
        } else {
            if (null != orderBy && orderBy.contains(CLASS)) {
                diseaseClass = diseaseClass.toLowerCase().contains(NOT) ? NUOT : diseaseClass;
                return diseaseItemRepository.findByDiseaseClassContainingOrderByDiseaseClass(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
            }
            return diseaseItemRepository.findByDiseaseClassContainingOrderByDiseaseName(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Page<DiseaseItem> findByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, Integer geneSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains(GENE)) {
            if (null != orderBy && orderBy.contains(DESC)) {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsDesc(diseaseName, geneSize, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsAsc(diseaseName, geneSize, createPageRequest(pageNumber, pageSize));
            }
        } else {
            if (null != orderBy && orderBy.contains(CLASS)) {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByDiseaseClass(diseaseName, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
            }
            return diseaseItemRepository.findByDiseaseNameContainingOrderByDiseaseName(diseaseName, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Integer getMaxGeneSize() {
        if (null == maxGeneSize) {
            maxGeneSize = diseaseItemRepository.findMaxGeneSize();
        }
        return maxGeneSize;
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize) {
        return createPageRequest(pageNumber, pageSize, null);
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize, Sort sort) {
        pageNumber = pageNumber == null ? 0 : pageNumber - 1; // the spring jpa page start from zero
        pageSize = pageSize == null ? 50 : pageSize;
        if (null != sort) {
            return PageRequest.of(pageNumber, pageSize, sort);
        } else {
            return PageRequest.of(pageNumber, pageSize);
        }
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = DISEASE_NAME;
        if (null != sortBy && sortBy.contains(CLASS)) {
            sortProperty = DISEASE_CLASS;
        }
        if (null != orderBy && orderBy.contains(DESC)) {
            sortDirection = Sort.Direction.DESC;
        }
        return createPageRequest(pageNumber, pageSize, Sort.by(sortDirection, sortProperty));
    }
}
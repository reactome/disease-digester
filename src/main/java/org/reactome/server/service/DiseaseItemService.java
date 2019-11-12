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
    private DiseaseItemRepository diseaseItemRepository;

    @Autowired
    public DiseaseItemService(DiseaseItemRepository diseaseItemRepository) {
        this.diseaseItemRepository = diseaseItemRepository;
    }

    public Page<DiseaseItem> findAll(Integer pageNumber, Integer pageSize, Integer geneSize, String sortBy, String orderBy) {
        // TODO: 2019/11/12 the if condition loop may instead by some switch case with Enum class
        if (null != sortBy && sortBy.contains("gene")) {
            if (orderBy.contains("desc")) {
                return diseaseItemRepository.findAllOrderByGeneItemsDesc(geneSize, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findAllOrderByGeneItemsAsc(geneSize, createPageRequest(pageNumber, pageSize));
            }
        } else {
            return diseaseItemRepository.findAllLimitedByGeneSize(geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Page<DiseaseItem> findByDiseaseClass(String diseaseClass, Integer pageNumber, Integer pageSize, Integer geneSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains("gene")) {
            if (null != orderBy && orderBy.contains("desc")) {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsDesc(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsAsc(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize));
            }
        } else {
            if (null != orderBy && orderBy.contains("class")) {
                diseaseClass = diseaseClass.toLowerCase().contains("not") ? "Νot" : diseaseClass;
                return diseaseItemRepository.findByDiseaseClassContainingOrderByDiseaseClass(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
            }
            return diseaseItemRepository.findByDiseaseClassContainingOrderByDiseaseName(diseaseClass, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Page<DiseaseItem> findByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, Integer geneSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains("gene")) {
            if (null != orderBy && orderBy.contains("desc")) {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsDesc(diseaseName, geneSize, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsAsc(diseaseName, geneSize, createPageRequest(pageNumber, pageSize));
            }
        } else {
            if (null != orderBy && orderBy.contains("class")) {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByDiseaseClass(diseaseName, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
            }
            return diseaseItemRepository.findByDiseaseNameContainingOrderByDiseaseName(diseaseName, geneSize, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Integer getMaxGeneSize() {
        // TODO: 2019/11/12 cache this number in some where/class to save the resource
        return diseaseItemRepository.findMaxGeneSize();
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
        String sortProperty = "diseaseName";
        if (null != sortBy && sortBy.contains("class")) {
            sortProperty = "diseaseClass";
        }
        if (null != orderBy && orderBy.contains("desc")) {
            sortDirection = Sort.Direction.DESC;
        }
        return createPageRequest(pageNumber, pageSize, Sort.by(sortDirection, sortProperty));
    }
}
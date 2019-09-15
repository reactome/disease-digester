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

    public Page<DiseaseItem> findAll(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains("gene")) {
            if (orderBy.contains("desc")) {
                return diseaseItemRepository.findAllOrderByGeneItemsDesc(createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findAllOrderByGeneItemsAsc(createPageRequest(pageNumber, pageSize));
            }
        } else {
            return diseaseItemRepository.findAll(createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Page<DiseaseItem> findByDiseaseClass(String diseaseClass, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains("gene")) {
            if (null != orderBy && orderBy.contains("desc")) {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsDesc(diseaseClass, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsAsc(diseaseClass, createPageRequest(pageNumber, pageSize));
            }
        } else {
            return diseaseItemRepository.findByDiseaseClassContainingOrderByDiseaseName(diseaseClass, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    public Page<DiseaseItem> findByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains("gene")) {
            if (null != orderBy && orderBy.contains("desc")) {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsDesc(diseaseName, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsAsc(diseaseName, createPageRequest(pageNumber, pageSize));
            }
        } else {
            return diseaseItemRepository.findByDiseaseNameContainingOrderByDiseaseName(diseaseName, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        }
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize) {
        return createPageRequest(pageNumber, pageSize, null);
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize, Sort sort) {
        pageNumber = pageNumber == null ? 0 : pageNumber;
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
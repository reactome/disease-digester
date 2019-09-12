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
        if (null != sortBy && sortBy.contains("disease")) {
            return diseaseItemRepository.findByDiseaseClassContainingOrderByDiseaseName(diseaseClass, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        } else {
            if (null != orderBy && orderBy.contains("desc")) {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsDesc(diseaseClass, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseClassContainingOrderByGeneItemsAsc(diseaseClass, createPageRequest(pageNumber, pageSize));
            }
        }
    }

    public Page<DiseaseItem> findByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        if (null != sortBy && sortBy.contains("disease")) {
            return diseaseItemRepository.findByDiseaseNameContainingOrderByDiseaseName(diseaseName, createPageRequest(pageNumber, pageSize, sortBy, orderBy));
        } else {
            if (null != orderBy && orderBy.contains("desc")) {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsDesc(diseaseName, createPageRequest(pageNumber, pageSize));
            } else {
                return diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsAsc(diseaseName, createPageRequest(pageNumber, pageSize));
            }
        }
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber == null ? 0 : pageNumber;
        pageSize = pageSize == null ? 50 : pageSize;
        return PageRequest.of(pageNumber, pageSize);
    }

    private Pageable createPageRequest(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String sortProperty = "diseaseName";
        if (null != sortBy) {
            if (sortBy.contains("class")) {
                sortProperty = "diseaseClass";
            }
        }
        if (null != orderBy) {
            if (orderBy.contains("desc")) {
                sortDirection = Sort.Direction.DESC;
            }
        }
        pageNumber = pageNumber == null ? 0 : pageNumber;
        pageSize = pageSize == null ? 50 : pageSize;
        return PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortProperty));
    }
}

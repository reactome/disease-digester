package org.reactome.server.service;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.PaginationResult;
import org.reactome.server.repository.DiseaseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
@Transactional
public class DiseaseItemService {

    private final DiseaseItemRepository diseaseItemRepository;
    private final EntityManager entityManager;

    @Autowired
    public DiseaseItemService(DiseaseItemRepository diseaseItemRepository, EntityManager entityManager) {
        this.diseaseItemRepository = diseaseItemRepository;
        this.entityManager = entityManager;
    }

    public List<DiseaseItem> findAll() {
        return diseaseItemRepository.findAll();
    }

    public DiseaseItem save(DiseaseItem diseaseItem) {
        diseaseItemRepository.save(diseaseItem);
        return diseaseItem;
    }

    public PaginationResult getPaginationResult(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DiseaseItem> query = criteriaBuilder.createQuery(DiseaseItem.class);
        Root<DiseaseItem> from = query.from(DiseaseItem.class);
        CriteriaQuery<DiseaseItem> select = query.select(from);
        TypedQuery<DiseaseItem> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(DiseaseItem.class)));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();
        return new PaginationResult(pageNumber, pageSize, totalCount, typedQuery.getResultList());
    }
}
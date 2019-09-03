package org.reactome.server.repository;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.PaginationResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Byron
 */
@Repository
public class DiseaseItemRepositoryCustomImpl implements DiseaseItemRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PaginationResult getPaginationResult(Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DiseaseItem> query = criteriaBuilder.createQuery(DiseaseItem.class);
        Root<DiseaseItem> diseaseItem = query.from(DiseaseItem.class);
        CriteriaQuery<DiseaseItem> select = query.select(diseaseItem);

        TypedQuery<DiseaseItem> typedQuery = sortAndOrderBy(sortBy, orderBy, criteriaBuilder, diseaseItem, select);

        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        // if this query can be cached?
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(DiseaseItem.class)));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PaginationResult(pageNumber, pageSize, totalCount, sortBy, orderBy, typedQuery.getResultList());
    }


    @Override
    public PaginationResult getPaginationResultByDiseaseClass(String diseaseClass, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DiseaseItem> query = criteriaBuilder.createQuery(DiseaseItem.class);
        Root<DiseaseItem> diseaseItem = query.from(DiseaseItem.class);
        CriteriaQuery<DiseaseItem> select = query.select(diseaseItem)
                .where(criteriaBuilder.like(diseaseItem.get("diseaseClass"), "%" + diseaseClass + "%"));

        return createQuery(pageNumber, pageSize, sortBy, orderBy, criteriaBuilder, diseaseItem, select);
    }

    @Override
    public PaginationResult getPaginationResultByDiseaseName(String diseaseName, Integer pageNumber, Integer pageSize, String sortBy, String orderBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DiseaseItem> query = criteriaBuilder.createQuery(DiseaseItem.class);
        Root<DiseaseItem> diseaseItem = query.from(DiseaseItem.class);
        CriteriaQuery<DiseaseItem> select = query.select(diseaseItem)
                .where(criteriaBuilder.like(diseaseItem.get("diseaseName"), "%" + diseaseName + "%"));

        return createQuery(pageNumber, pageSize, sortBy, orderBy, criteriaBuilder, diseaseItem, select);
    }

    private PaginationResult createQuery(Integer pageNumber, Integer pageSize, String sortBy, String orderBy, CriteriaBuilder criteriaBuilder, Root<DiseaseItem> diseaseItem, CriteriaQuery<DiseaseItem> select) {
        TypedQuery<DiseaseItem> typedQuery = sortAndOrderBy(sortBy, orderBy, criteriaBuilder, diseaseItem, select);
        // TODO: 19-8-29 here the every time will check all results and but only return partition and discard the surplus which is not effective
        //  but dont know if those query results can br cached
        typedQuery.setFirstResult((pageNumber - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);
        List<DiseaseItem> resultList = typedQuery.getResultList();
        long totalCount = resultList.size();
        return new PaginationResult(pageNumber, pageSize, totalCount, sortBy, orderBy, resultList);
    }

    private TypedQuery<DiseaseItem> sortAndOrderBy(String sortBy, String orderBy, CriteriaBuilder criteriaBuilder, Root<DiseaseItem> diseaseItem, CriteriaQuery<DiseaseItem> select) {
        if (null != sortBy) {
            if (sortBy.contains("disease")) {
                if (null != orderBy) {
                    if (orderBy.contains("asc")) {
                        select.orderBy(criteriaBuilder.asc(diseaseItem.get("diseaseName")));
                    } else {
                        select.orderBy(criteriaBuilder.desc(diseaseItem.get("diseaseName")));
                    }
                }
            } else if (sortBy.contains("gene")) {
                if (null != orderBy) {
                    if (orderBy.contains("asc")) {
                        select.orderBy(criteriaBuilder.asc(criteriaBuilder.size(diseaseItem.get("geneItems"))));
                    } else {
                        select.orderBy(criteriaBuilder.desc(criteriaBuilder.size(diseaseItem.get("geneItems"))));
                    }
                }
            }
        }
        return entityManager.createQuery(select);
    }
}

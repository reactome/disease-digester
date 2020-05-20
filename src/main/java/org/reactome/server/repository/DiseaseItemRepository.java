package org.reactome.server.repository;

import org.reactome.server.domain.DiseaseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DiseaseItemRepository extends JpaRepository<DiseaseItem, Long> {

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.geneItems.size >= :geneSize")
    Page<DiseaseItem> findAllLimitedByGeneSize(@Param("geneSize") Integer geneSize, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseName LIKE %:diseaseName% AND d.geneItems.size >= :geneSize")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByDiseaseName(@Param("diseaseName") String diseaseName, @Param("geneSize") Integer geneSize, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.geneItems.size >= :geneSize ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findAllOrderByGeneItemsDesc(@Param("geneSize") Integer geneSize, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.geneItems.size >= :geneSize ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findAllOrderByGeneItemsAsc(@Param("geneSize") Integer geneSize, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.diseaseName LIKE %:diseaseName% AND d.geneItems.size >= :geneSize ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByGeneItemsAsc(@Param("diseaseName") String diseaseName, @Param("geneSize") Integer geneSize, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.diseaseName LIKE %:diseaseName% AND d.geneItems.size >= :geneSize ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByGeneItemsDesc(@Param("diseaseName") String diseaseName, @Param("geneSize") Integer geneSize, Pageable pageable);

    @Query(value = "SELECT MAX(d.geneItems.size) FROM DiseaseItem d")
    Integer findMaxGeneSize();

    DiseaseItem findDiseaseItemByDiseaseId(String diseaseId);
}
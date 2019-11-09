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

    // TODO: 2019/11/8 add the filter by the size of geneItems
    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseClass LIKE %:diseaseClass%")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByDiseaseName(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseClass LIKE %:diseaseClass%")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByDiseaseClass(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseName LIKE %:diseaseName%")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByDiseaseName(@Param("diseaseName") String diseaseName, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseName LIKE %:diseaseName%")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByDiseaseClass(@Param("diseaseName") String diseaseName, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findAllOrderByGeneItemsDesc(Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findAllOrderByGeneItemsAsc(Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.diseaseClass LIKE %:diseaseClass% ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByGeneItemsAsc(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.diseaseClass LIKE %:diseaseClass% ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByGeneItemsDesc(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.diseaseName LIKE %:diseaseName% ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByGeneItemsAsc(@Param("diseaseName") String diseaseName, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d WHERE d.diseaseName LIKE %:diseaseName% ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByGeneItemsDesc(@Param("diseaseName") String diseaseName, Pageable pageable);
}
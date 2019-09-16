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

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseClass LIKE %:diseaseClass%")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByDiseaseName(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem  d WHERE d.diseaseName LIKE %:diseaseName%")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByDiseaseName(@Param("diseaseName") String diseaseName, Pageable pageable);

    // TODO: 2019/9/16 "java.sql.SQLSyntaxErrorException:
    //  Expression #1 of SELECT list is not in GROUP BY clause and contains nonaggregated column 'digester.diseaseite0_.id'
    //  which is not functionally dependent on columns in GROUP BY clause; this is incompatible with sql_mode=only_full_group_by"
    //  the use of "group by clause will raise sql_mode incompatible exception since mysql5.7.X not support ambiguity query by default"
    //  two option solve it:
    //      1. set `session sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';`
    //      2. fix error in query by using without any ambiguity field
    @Query(value = "SELECT d FROM DiseaseItem d GROUP BY d.diseaseId ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findAllOrderByGeneItemsDesc(Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d GROUP BY d.diseaseId ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findAllOrderByGeneItemsAsc(Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d " +
            "WHERE d.diseaseClass LIKE %:diseaseClass% " +
            "GROUP BY d.diseaseId " +
            "ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByGeneItemsAsc(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d " +
            "WHERE d.diseaseClass LIKE %:diseaseClass% " +
            "GROUP BY d.diseaseId " +
            "ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findByDiseaseClassContainingOrderByGeneItemsDesc(@Param("diseaseClass") String diseaseClass, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d " +
            "WHERE d.diseaseName LIKE %:diseaseName% " +
            "GROUP BY d.diseaseId " +
            "ORDER BY d.geneItems.size ASC")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByGeneItemsAsc(@Param("diseaseName") String diseaseName, Pageable pageable);

    @Query(value = "SELECT d FROM DiseaseItem d " +
            "WHERE d.diseaseName LIKE %:diseaseName% " +
            "GROUP BY d.diseaseId " +
            "ORDER BY d.geneItems.size DESC")
    Page<DiseaseItem> findByDiseaseNameContainingOrderByGeneItemsDesc(@Param("diseaseName") String diseaseName, Pageable pageable);
}
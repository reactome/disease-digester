package org.reactome.server.repository;

import org.apache.ibatis.annotations.Param;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.repository.out.GeneToDiseases;
import org.reactome.server.repository.out.Sorted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Set;

@Repository
public interface DiseaseRepository extends CrudRepository<Disease, String> {

    @Query(value =
            "        SELECT g.accessionNumber as acc, " +
                    "       COUNT(d.diseaseId) as count, " +
                    "       JSON_ARRAYAGG( " +
                    "           JSON_OBJECT( " +
                    "               'acc', d.diseaseId, " +
                    "               'alias', d.diseaseName, " +
                    "               'score', TRUNCATE(gda.score, 3), " +
                    "               'accURL', CONCAT('https://www.disgenet.org/search?disease=', d.diseaseId), " +
                    "               'evidencesURL', CONCAT('https://www.disgenet.org/browser/1/1/1/', g.geneId, '/0/25/diseaseid__', d.diseaseId, '-source__ALL/_b./') " +
                    "               ) " +
                    "           ) as interactors " +
                    "FROM disease d " +
                    "         INNER JOIN gda gda on d.diseaseId = gda.diseaseId " +
                    "         INNER JOIN gene g on gda.geneId = g.geneId " +
                    "WHERE g.accessionNumber IN :geneAcs " +
                    "GROUP BY g.accessionNumber ", nativeQuery = true)
    List<Tuple> findByGenes(@Param("geneAcs") Set<String> geneAcs);

    @Query("select d as result, COUNT(gda.gene) as associatedGenesCount " +
            "from Disease d " +
            "inner join GDA gda on d.diseaseId = gda.disease " +
            "inner join Gene g on gda.gene = g.geneId " +
            "where gda.score >= :score " +
            "group by d.diseaseId " +
            "having count(g.geneId) >= :size ")
    Page<Sorted<Disease>> findAll(@Param("score") Float score, @Param("size") Long size, Pageable pageable);

    @Query("select d as result, count(gda.gene) as associatedGenesCount " +
            "from Disease d " +
            "inner join GDA gda on d.diseaseId = gda.disease " +
            "inner join Gene g on gda.gene = g.geneId " +
            "where d.diseaseName like concat('%', :diseaseName ,'%') " +
            "and gda.score >= :score " +
            "group by d.diseaseId " +
            "having count(g.geneId) >= :size ")
    Page<Sorted<Disease>> findByDiseaseName(@Param("diseaseName") String diseaseName, @Param("score") Float score, @Param("size") Long size, Pageable pageable);

    @Query("select count(d.diseaseId), count(gda) as associatedGenesCount " +
            "from Disease d " +
            "inner join GDA gda on d.diseaseId = gda.disease " +
            "inner join Gene g on gda.gene = g.geneId " +
            "where gda.score >= :score " +
            "group by d.diseaseId " +
            "having count(g.geneId) >= :size")
    Long selectDiseaseIdCount(@Param("score") Float score, @Param("size") Long size);

    @Query(value =
            "select max(c) " +
                    "from (" +
                    "select count(g.geneId) c " +
                    "from Disease d " +
                    "inner join gda gda on d.diseaseId = gda.diseaseId " +
                    "inner join gene g on gda.geneId = g.geneId " +
                    "where gda.score >= :score " +
                    "group by d.diseaseId" +
                    ") _"
            , nativeQuery = true)
    Long selectMaxGeneSizeByScore(@Param("score") Float score);

    @Query(value =
            "select max(c) " +
                    "from (" +
                    "select count(g.geneId) c " +
                    "from disease d " +
                    "inner join gda gda on d.diseaseId = gda.diseaseId " +
                    "inner join gene g on gda.geneId = g.geneId " +
                    "where d.diseaseName like concat('%', :diseaseName,'%') " +
                    "and gda.score >= :score " +
                    "group by d.diseaseId) _"
            , nativeQuery = true)
    Long selectMaxGeneSizeByScoreAndDiseaseName(@Param("diseaseName") String diseaseName, @Param("score") Float score);

    @Query("select d.diseaseName" +
            " from Disease d order by d.diseaseName ASC ")
    List<String> selectDiseaseName();

    @Query("select g.geneSymbol " +
            "from Gene g " +
            "inner join GDA gda on gda.gene = g.geneId " +
            "where gda.disease = :diseaseId")
    List<String> getGeneListByDiseaseId(@Param("diseaseId") String diseaseId);
}



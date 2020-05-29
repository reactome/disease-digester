package org.reactome.server.mapper;

import org.apache.ibatis.annotations.*;
import org.reactome.server.domain.DiseaseItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Mapper
@Component
public interface DiseaseItemMapper {
    @Select("select d.diseaseId from disease d inner join gene g on d.diseaseId = g.diseaseId where score >= ${score} group by d.diseaseId having count(g.geneId) >= ${size} order by count(g.geneId) ${order} limit ${pageSize} offset ${offset}")
    List<String> selectDiseaseId(@Param("score") Float score, @Param("size") Integer geneSize, @Param("order") String order, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("select count(d.diseaseId) from disease d inner join gene g on d.diseaseId = g.diseaseId where score >= ${score} group by d.diseaseId having count(g.geneId) >= ${size}")
    List<Integer> selectDiseaseIdCount(@Param("score") Float score, @Param("size") Integer geneSize);

    @Select("select d.diseaseId from disease d inner join gene g on d.diseaseId = g.diseaseId where diseaseName like concat('%','${diseaseName}','%') and score >= ${score} group by d.diseaseId having count(g.geneId) >= ${size} order by count(g.geneId) ${order} limit ${pageSize} offset ${offset}")
    List<String> selectDiseaseIdByDiseaseName(@Param("diseaseName") String diseaseName, @Param("score") Float score, @Param("size") Integer geneSize, @Param("order") String order, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("select count(d.diseaseId) from disease d inner join gene g on d.diseaseId = g.diseaseId where diseaseName like concat('%','${diseaseName}','%') and score >= ${score} group by d.diseaseId having count(g.geneId) >= ${size}")
    List<Integer> selectDiseaseIdByDiseaseNameCount(@Param("diseaseName") String diseaseName, @Param("score") Float score, @Param("size") Integer geneSize);

    @Select("select d.diseaseId from disease d inner join gene g on d.diseaseId = g.diseaseId where score >= ${score} group by d.diseaseId having count(g.geneId) >= ${size} order by d.diseaseName ${order} limit ${pageSize} offset ${offset}")
    List<String> selectDiseaseIdOrderByDiseaseName(@Param("score") Float score, @Param("size") Integer geneSize, @Param("order") String order, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("select d.diseaseId from disease d inner join gene g on d.diseaseId = g.diseaseId where diseaseName like concat('%','${diseaseName}','%') score >= ${score} group by d.diseaseId having count(g.geneId) >= ${size} order by d.diseaseName ${order} limit ${pageSize} offset ${offset}")
    List<String> selectDiseaseIdByDiseaseNameAndOrderByDiseaseName(@Param("diseaseName") String diseaseName, @Param("score") Float score, @Param("size") Integer geneSize, @Param("order") String order, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("select max(c) from (select count(g.geneId) c from disease d inner join gene g on d.diseaseId = g.diseaseId where score >= ${score} group by d.diseaseId) _")
    Integer selectMaxGeneSizeByScore(@Param("score") Float score);

    @Select("select max(c) from (select count(g.geneId) c from disease d inner join gene g on d.diseaseId = g.diseaseId where diseaseName like concat('%','${diseaseName}','%') and score >= ${score} group by d.diseaseId) _")
    Integer selectMaxGeneSizeByScoreAndDiseaseName(@Param("diseaseName") String diseaseName, @Param("score") Float score);

    @Select("<script> " +
            "select diseaseId,diseaseName,${score} score from disease where diseaseId in " +
            "<foreach collection='diseaseIds' item='diseaseId' index='index' open='(' separator=',' close=')'> '${diseaseId}' </foreach> " +
            "</script>")
    @Results(id = "diseaseItemMap",
            value = {@Result(property = "geneItems", javaType = ArrayList.class, column = "{diseaseId=diseaseId, score=score}", many = @Many(select = "org.reactome.server.mapper.GeneItemMapper.findGeneItemByDiseaseIdAndScore"))})
    List<DiseaseItem> findAllByDiseaseIdAndScore(@Param("diseaseIds") List<String> diseaseIds, @Param("score") float score);

    @Select("select diseaseName from disease")
    List<String> selectDiseaseName();

    @Select("select geneSymbol from gene where diseaseId = '${diseaseId}'")
    List<String> getGeneListByDiseaseId(@Param("diseaseId") String diseaseId);
}

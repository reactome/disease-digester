package org.reactome.server.mapper;

import org.apache.ibatis.annotations.*;
import org.reactome.server.domain.GeneItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
@CacheNamespace
public interface GeneItemMapper {
    @Select("select geneSymbol,score from gene where score >= ${score} and diseaseId = '${diseaseId}' ")
    List<GeneItem> findGeneItemByDiseaseIdAndScore(@Param("diseaseId") String diseaseId, @Param("score") float score);
}

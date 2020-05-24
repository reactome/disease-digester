package org.reactome.server.util;

import org.reactome.server.mapper.DiseaseItemMapper;

import java.util.concurrent.ConcurrentHashMap;

public class PageHelper {
    private static ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<>();

    private PageHelper() {
    }

    public static Integer getCountWithDiseaseName(DiseaseItemMapper mapper, String diseaseName, Float score, Integer geneSize) {
        String key = diseaseName.concat(score.toString()).concat(geneSize.toString());
        if (countMap.containsKey(key)) {
            return countMap.get(key);
        }
        Integer count;
        synchronized (PageHelper.class) {
            count = mapper.selectDiseaseIdByDiseaseNameCount(diseaseName, score, geneSize).size();
        }
        countMap.put(key, count);
        return count;
    }

    public static Integer getCount(DiseaseItemMapper mapper, Float score, Integer geneSize) {
        String key = score.toString().concat(geneSize.toString());
        if (countMap.containsKey(key)) {
            return countMap.get(key);
        }
        Integer count;
        synchronized (PageHelper.class) {
            count = mapper.selectDiseaseIdCount(score, geneSize).size();
        }
        countMap.put(key, count);
        return count;
    }
}

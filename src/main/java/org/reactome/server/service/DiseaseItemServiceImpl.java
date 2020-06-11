package org.reactome.server.service;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.DiseaseItemResult;
import org.reactome.server.mapper.DiseaseItemMapper;
import org.reactome.server.util.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Byron
 */
@Service
public class DiseaseItemServiceImpl implements DiseaseItemService {
    private final DiseaseItemMapper diseaseItemMapper;

    @Autowired
    public DiseaseItemServiceImpl(DiseaseItemMapper diseaseItemMapper) {
        this.diseaseItemMapper = diseaseItemMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public DiseaseItemResult findAll(Integer pageNumber, Integer pageSize, Float score, Integer geneSize, SortBy sort, OrderBy order) {
        List<String> diseaseIds;
        List<DiseaseItem> diseaseItems;
        Integer count = PageHelper.getCount(diseaseItemMapper, score, geneSize);
        assert pageSize != 0;
        Integer totalPage = count / pageSize + 1;
        if (pageSize > count) {
            pageSize = count;
        }
        int offset;
        if (pageNumber > totalPage) {
            pageNumber = totalPage;
            offset = 0;
        } else {
            offset = (pageNumber - 1) * pageSize;
        }
        if (sort == SortBy.GENE) {
            diseaseIds = diseaseItemMapper.selectDiseaseId(score, geneSize, order.getOrder(), pageSize, offset);
        } else {
            diseaseIds = diseaseItemMapper.selectDiseaseIdOrderByDiseaseName(score, geneSize, order.getOrder(), pageSize, offset);
        }
        if (diseaseIds.size() == 0) {
            return new DiseaseItemResult();
        }
        diseaseItems = diseaseItemMapper.findAllByDiseaseIdAndScore(diseaseIds, score);
        return new DiseaseItemResult(diseaseItems, pageNumber, totalPage, sort, order);
    }

    @Override
    @Transactional(readOnly = true)
    public DiseaseItemResult findByDiseaseName(Integer pageNumber, Integer pageSize, String diseaseName, Float score, Integer geneSize, SortBy sort, OrderBy order) {
        List<String> diseaseIds;
        List<DiseaseItem> diseaseItems;
        Integer count = PageHelper.getCountWithDiseaseName(diseaseItemMapper, diseaseName, score, geneSize);
        assert pageSize != 0;
        Integer totalPage = count / pageSize + 1;
        if (pageSize > count) {
            pageSize = count;
        }
        int offset;
        if (pageNumber > totalPage) {
            pageNumber = totalPage;
            offset = 0;
        } else {
            offset = (pageNumber - 1) * pageSize;
        }
        if (sort == SortBy.GENE) {
            diseaseIds = diseaseItemMapper.selectDiseaseIdByDiseaseName(diseaseName, score, geneSize, order.getOrder(), pageSize, offset);
        } else {
            diseaseIds = diseaseItemMapper.selectDiseaseIdByDiseaseNameAndOrderByDiseaseName(diseaseName, score, geneSize, order.getOrder(), pageSize, offset);
        }
        if (diseaseIds.size() == 0) {
            return new DiseaseItemResult();
        }
        diseaseItems = diseaseItemMapper.findAllByDiseaseIdAndScore(diseaseIds, score);
        return new DiseaseItemResult(diseaseItems, pageNumber, totalPage, sort, order);
    }

    public Integer getMaxGeneSize(Float score, String diseaseName) {
        if (null == diseaseName) {
            return diseaseItemMapper.selectMaxGeneSizeByScore(score);
        } else {
            return diseaseItemMapper.selectMaxGeneSizeByScoreAndDiseaseName(diseaseName, score);
        }
    }

    public List<String> getGeneListByDiseaseId(String diseaseId) {
        return diseaseItemMapper.getGeneListByDiseaseId(diseaseId);
    }
}
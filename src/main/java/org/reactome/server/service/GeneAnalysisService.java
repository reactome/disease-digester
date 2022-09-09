package org.reactome.server.service;


import org.reactome.server.domain.analysis.AnalysisRequestData;
import org.reactome.server.domain.analysis.AnalysisResult;
import org.reactome.server.domain.analysis.Resource;
import org.reactome.server.domain.analysis.SortBy;
import org.reactome.server.domain.model.SourceDatabase;
import org.reactome.server.exception.FailedAnalyzeDiseaseException;

public interface GeneAnalysisService {
    String checkGeneListAnalysisResult(AnalysisRequestData requestData, String baseUrl) throws FailedAnalyzeDiseaseException;

    AnalysisResult analysisByDisease(String disease, Boolean projection, Boolean interactors, SortBy sortBy, OrderBy order, Resource resource, Float pValue, Boolean includeDiseasePathways, SourceDatabase source, String baseUrl);
}
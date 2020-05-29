package org.reactome.server.service;


import org.reactome.server.domain.analysis.*;
import org.reactome.server.domain.analysis.SortBy;
import org.reactome.server.exception.FailedAnalyzeDiseaseException;

public interface GeneAnalysisService {
    String checkGeneListAnalysisResult(AnalysisRequestData requestData) throws FailedAnalyzeDiseaseException;

    AnalysisResult analysisByDisease(String disease, Boolean projection, Boolean interactors, SortBy sortBy, OrderBy order, Resource resource, Float pValue, Boolean includeDisease) throws FailedAnalyzeDiseaseException;
}
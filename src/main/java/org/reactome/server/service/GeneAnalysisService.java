package org.reactome.server.service;


import org.reactome.server.domain.analysis.*;
import org.reactome.server.domain.analysis.SortBy;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;

public interface GeneAnalysisService {
    String checkGeneListAnalysisResult(AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException;

    AnalysisResult analysisByDiseaseId(String diseaseId, Boolean projection, Boolean interactors, SortBy sortBy, OrderBy order, Resource resource, Float pValue, Boolean includeDisease) throws EmptyGeneAnalysisResultException;
}
package org.reactome.server.service;


import org.reactome.server.domain.AnalysisResult;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.reactome.server.util.ReactomeURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeneAnalysisService {
    private final RestTemplate restTemplate;

    @Autowired
    public GeneAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // todo: use reactome analysis-service component instead of request data via API from network
    public String checkGeneAnalysisResult(List<String> geneList) throws EmptyGeneAnalysisResultException {
        String payload = String.join(" ", geneList);
        AnalysisResult result = restTemplate.postForObject(ReactomeURL.ANALYSIS_SERVICE, payload, AnalysisResult.class);
        String token;
        if (result != null) {
            token = result.getSummary().getToken();
        } else {
            throw new EmptyGeneAnalysisResultException(payload);
        }
        return ReactomeURL.PATHWAY_BROWSER + token;
    }
}
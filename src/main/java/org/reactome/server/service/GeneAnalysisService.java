package org.reactome.server.service;


import org.reactome.server.domain.AnalysisResult;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

@Service
public class GeneAnalysisService {
    private final RestTemplate restTemplate;

    @Value("${reactome.pathway.browser}")
    private String PATHWAY_BROWSER;

    @Value("${reactome.analysis.service}")
    private String ANALYSIS_SERVICE;

    @Autowired
    public GeneAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // todo: use reactome analysis-service component instead of request data via API from network
    public String checkGeneListAnalysisResult(String[] genes) throws EmptyGeneAnalysisResultException {
        String payload = Stream.of(genes).reduce(" ", String::concat);
        AnalysisResult result = restTemplate.postForObject(ANALYSIS_SERVICE, payload, AnalysisResult.class);
        String token;
        if (result != null) {
            token = result.getSummary().getToken();
        } else {
            throw new EmptyGeneAnalysisResultException(payload);
        }
        return PATHWAY_BROWSER + token;
    }
}
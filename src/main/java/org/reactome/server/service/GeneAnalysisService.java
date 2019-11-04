package org.reactome.server.service;


import org.reactome.server.domain.AnalysisGeneList;
import org.reactome.server.domain.AnalysisResult;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public String checkGeneListAnalysisResult(AnalysisGeneList geneList) throws EmptyGeneAnalysisResultException {
        String payload = String.join(" ", geneList.getGenes());
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
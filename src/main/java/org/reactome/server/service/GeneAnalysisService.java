package org.reactome.server.service;


import org.apache.commons.lang3.StringUtils;
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
    private static final String DEFAULT_ANALYSIS_PARAMETER = "projection";

    @Value("${reactome.pathway.browser}")
    private String PATHWAY_BROWSER;

    @Value("${reactome.analysis.service.with.interactors}")
    private String ANALYSIS_SERVICE_WITH_INTERACTORS;

    @Value("${reactome.analysis.service.with.projection}")
    private String ANALYSIS_SERVICE_WITH_PROJECTION;

    @Autowired
    public GeneAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // todo: use reactome analysis-service component instead of request data via API from network
    public String checkGeneListAnalysisResult(AnalysisGeneList geneList) throws EmptyGeneAnalysisResultException {
        String payload = String.join(" ", geneList.getGenes());
        AnalysisResult result;
        // TODO: 2019/11/8 the condition loop should be more generic
        if (StringUtils.containsIgnoreCase(geneList.getAnalysisParameter(), DEFAULT_ANALYSIS_PARAMETER)) {
            result = restTemplate.postForObject(ANALYSIS_SERVICE_WITH_PROJECTION, payload, AnalysisResult.class);
        } else {
            result = restTemplate.postForObject(ANALYSIS_SERVICE_WITH_INTERACTORS, payload, AnalysisResult.class);
        }
        String token;
        if (result != null) {
            token = result.getSummary().getToken();
        } else {
            throw new EmptyGeneAnalysisResultException(payload);
        }
        return PATHWAY_BROWSER.concat(token);
    }
}
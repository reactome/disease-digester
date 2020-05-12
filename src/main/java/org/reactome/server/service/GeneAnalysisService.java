package org.reactome.server.service;


import org.apache.commons.lang3.StringUtils;
import org.reactome.server.domain.AnalysisRequestData;
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

    @Value("${reactome.pathway.browser.fireworks}")
    private String PATHWAY_BROWSER_FIREWORKS;
    @Value("${reactome.pathway.browser.reacfoam}")
    private String PATHWAY_BROWSER_REACFOAM;

    @Value("${reactome.analysis.service}")
    private String ANALYSIS_SERVICE;

    @Value("${reactome.analysis.service.placeholder}")
    private String PLACEHOLDER;

    @Autowired
    public GeneAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // todo: use reactome analysis-service component instead of request data via API from network
    public String checkGeneListAnalysisResult(AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException {
        String payload = String.join(" ", requestData.getGenes());
        String analysisService = requestData.isProjectToHuman() ?
                StringUtils.replaceFirst(ANALYSIS_SERVICE, PLACEHOLDER, DEFAULT_ANALYSIS_PARAMETER) :
                StringUtils.replaceFirst(ANALYSIS_SERVICE, PLACEHOLDER, "");
        analysisService = StringUtils.replaceFirst(analysisService, PLACEHOLDER, requestData.isIncludeInteractors().toString());
        AnalysisResult result = restTemplate.postForObject(analysisService, payload, AnalysisResult.class);
        String token;
        if (null != result) {
            token = result.getSummary().getToken();
        } else {
            throw new EmptyGeneAnalysisResultException(payload);
        }
        return requestData.isRedirectToReacfoam()
                ? StringUtils.replaceFirst(PATHWAY_BROWSER_REACFOAM, PLACEHOLDER, token)
                : StringUtils.replaceFirst(PATHWAY_BROWSER_FIREWORKS, PLACEHOLDER, token);
    }
}
package org.reactome.server.service;


import org.apache.commons.lang3.StringUtils;
import org.reactome.server.domain.AnalysisRequestData;
import org.reactome.server.domain.AnalysisResult;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeneAnalysisService {
    private final RestTemplate restTemplate;
    private final DiseaseItemService diseaseItemService;
    private static final String DEFAULT_ANALYSIS_PARAMETER = "projection";
    private static final Logger logger = LoggerFactory.getLogger(GeneAnalysisService.class);

    @Value("${reactome.pathway.browser.fireworks}")
    private String PATHWAY_BROWSER_FIREWORKS;
    @Value("${reactome.pathway.browser.reacfoam}")
    private String PATHWAY_BROWSER_REACFOAM;

    @Value("${reactome.analysis.service}")
    private String ANALYSIS_SERVICE;

    @Value("${reactome.analysis.service.placeholder}")
    private String PLACEHOLDER;

    @Autowired
    public GeneAnalysisService(RestTemplate restTemplate, DiseaseItemService diseaseItemService) {
        this.restTemplate = restTemplate;
        this.diseaseItemService = diseaseItemService;
    }

    // todo: use reactome analysis-service component instead of request data via API from network
    public String checkGeneListAnalysisResult(AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException {
        String payLoad = String.join(" ", requestData.getGenes());
        String analysisService = requestData.isProjectToHuman() ?
                StringUtils.replaceFirst(ANALYSIS_SERVICE, PLACEHOLDER, DEFAULT_ANALYSIS_PARAMETER) :
                StringUtils.replaceFirst(ANALYSIS_SERVICE, PLACEHOLDER, "");
        analysisService = StringUtils.replaceFirst(analysisService, PLACEHOLDER, requestData.isIncludeInteractors().toString());
        String token = doAnalysis(analysisService, payLoad);
        return requestData.isRedirectToReacfoam()
                ? StringUtils.replaceFirst(PATHWAY_BROWSER_REACFOAM, PLACEHOLDER, token)
                : StringUtils.replaceFirst(PATHWAY_BROWSER_FIREWORKS, PLACEHOLDER, token);
    }

    public String analysisByDiseaseName(String disease) throws EmptyGeneAnalysisResultException {
        String payLoad = String.join(" ", diseaseItemService.getGeneItemsByDiseaseId(disease));
        String url = StringUtils.replaceFirst(ANALYSIS_SERVICE, PLACEHOLDER, DEFAULT_ANALYSIS_PARAMETER);
        url = StringUtils.replaceFirst(url, PLACEHOLDER, "true");
        logger.info(url);
        logger.info(payLoad);
        String token = doAnalysis(url, payLoad);
        return StringUtils.replace(PATHWAY_BROWSER_REACFOAM, PLACEHOLDER, token);
    }

    private String doAnalysis(String url, String playLoad) throws EmptyGeneAnalysisResultException {
        AnalysisResult result = restTemplate.postForObject(url, playLoad, AnalysisResult.class);
        String token = null;
        if (null != result) {
            token = result.getSummary().getToken();
        } else {
            throw new EmptyGeneAnalysisResultException(playLoad);
        }
        return token;
    }
}
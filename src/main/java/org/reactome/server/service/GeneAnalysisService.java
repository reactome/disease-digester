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
    private RestTemplate restTemplate;
    private DiseaseItemService diseaseItemService;
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

    // todo: use reactome analysis-service as internal component instead of request data via API from network
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

    // TODO: 2020/5/24 add more parameter in this method
    public String analysisByDiseaseId(String diseaseId) throws EmptyGeneAnalysisResultException {
        String payLoad = String.join(" ", diseaseItemService.getGeneListByDiseaseId(diseaseId));
        String url = StringUtils.replaceFirst(ANALYSIS_SERVICE, PLACEHOLDER, DEFAULT_ANALYSIS_PARAMETER);
        url = StringUtils.replaceFirst(url, PLACEHOLDER, "true");
        String token = doAnalysis(url, payLoad);
        return StringUtils.replace(PATHWAY_BROWSER_REACFOAM, PLACEHOLDER, token);
    }

    //Name	Description
//file *
//file
//(formData)
//A file with the data to be analysed
//
//interactors
//boolean
//(query)
//Include interactors
//
//pageSize
//integer
//(query)
//pathways per page
//
//page
//integer
//(query)
//page number
//
//sortBy
//string
//(query)
//how to sort the result
//
//order
//string
//(query)
//specifies the order
//
//resource
//string
//(query)
//the resource to sort
//
//pValue
//number
//(query)
//defines the pValue threshold. Only hit pathway with pValue equals or below the threshold will be returned
//
//includeDisease
//boolean
//(query)
//set to ‘false’ to exclude the disease pathways from the result (it does not alter the statistics)
//
//min
//integer
//(query)
//minimum number of contained entities per pathway (takes into account the resource)
//
//max
//integer
//(query)
//maximum number of contained entities per pathway (takes into account the resource)
//    todo: take those above parameter into account :https://reactome.org/AnalysisService/#/identifiers/getPostFileToHumanUsingPOST
    private String doAnalysis(String url, String playLoad) throws EmptyGeneAnalysisResultException {
        AnalysisResult result = restTemplate.postForObject(url, playLoad, AnalysisResult.class);
        String token;
        if (null != result) {
            token = result.getSummary().getToken();
        } else {
            throw new EmptyGeneAnalysisResultException(String.format("Failed to analysis data from URL: %s with payload: %s.", url, playLoad));
        }
        return token;
    }
}
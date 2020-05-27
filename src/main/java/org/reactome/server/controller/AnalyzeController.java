package org.reactome.server.controller;

import org.reactome.server.domain.analysis.AnalysisParameter;
import org.reactome.server.domain.analysis.AnalysisRequestData;
import org.reactome.server.domain.analysis.Resource;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.reactome.server.service.GeneAnalysisService;
import org.reactome.server.service.OrderBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/analyze")
public class AnalyzeController {

    private final GeneAnalysisService geneAnalysisService;

    public AnalyzeController(GeneAnalysisService geneAnalysisService) {
        this.geneAnalysisService = geneAnalysisService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String analyse(@RequestBody AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException {
        return geneAnalysisService.checkGeneListAnalysisResult(requestData);
    }


    @RequestMapping(value = "/{diseaseId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    AnalysisParameter diseaseAnalysis(@PathVariable("diseaseId") String diseaseId,
                                      @RequestParam(value = "projection", required = false) Boolean projection,
                                      @RequestParam(value = "interactors", required = false) Boolean interactors,
                                      @RequestParam(value = "sortBy", required = false) org.reactome.server.domain.analysis.SortBy sortBy,
                                      @RequestParam(value = "order", required = false) OrderBy order,
                                      @RequestParam(value = "resource", required = false) Resource resource,
                                      @RequestParam(value = "pValue", required = false) Float pValue,
                                      @RequestParam(value = "includeDisease", required = false) Boolean includeDisease) throws EmptyGeneAnalysisResultException {
        return geneAnalysisService.analysisByDiseaseId(diseaseId, projection, interactors, sortBy, order, resource, pValue, includeDisease);
    }
}
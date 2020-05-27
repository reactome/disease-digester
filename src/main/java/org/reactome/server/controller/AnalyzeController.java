package org.reactome.server.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.reactome.server.domain.analysis.AnalysisRequestData;
import org.reactome.server.domain.analysis.AnalysisResult;
import org.reactome.server.domain.analysis.Resource;
import org.reactome.server.domain.analysis.SortBy;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.reactome.server.service.GeneAnalysisService;
import org.reactome.server.service.OrderBy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/analyze")
public class AnalyzeController {

    private final GeneAnalysisService geneAnalysisService;

    public AnalyzeController(GeneAnalysisService geneAnalysisService) {
        this.geneAnalysisService = geneAnalysisService;
    }

    @ApiIgnore
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String analyse(@RequestBody AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException {
        return geneAnalysisService.checkGeneListAnalysisResult(requestData);
    }


    @GetMapping(value = "/{diseaseId}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Do analysis by given a specific diseases id and return the reacfoam result link.",
            notes = "Use projection to analyze the disease id's associated identifiers retrieved from database over different species and project the result to Homo Sapiens(the projection is calculated by the orthologous slot in the Reactome database). " +
                    "Use interactors to decide if include interactors. " +
                    "the defines the pValue threshold. Only hit pathway with pValue equals or below the threshold will be returned. " +
                    "The includeDisease set to ‘false’ to exclude the disease pathways from the result (it does not alter the statistics) ")
    public @ResponseBody
    AnalysisResult diseaseAnalysis(@ApiParam(name = "diseaseId", value = "A specific disease id", required = true) @PathVariable("diseaseId") String diseaseId,
                                   @ApiParam(name = "projection", value = "If project result to Homo Sapiens") Boolean projection,
                                   @ApiParam(name = "interactors", value = "Include interactors") Boolean interactors,
//                                   @ApiParam(name = "sortBy") org.reactome.server.domain.analysis.SortBy sortBy,
//                                   @ApiParam(name = "order") OrderBy order,
//                                   @ApiParam(name = "resource", value = "") Resource resource,
                                   @ApiParam(name = "pValue", value = "defines the pValue threshold. Only hit pathway with pValue equals or below the threshold will be returned") Float pValue,
                                   @ApiParam(name = "includeDisease", value = "set to ‘false’ to exclude the disease pathways from the result (it does not alter the statistics)\n") Boolean includeDisease) throws EmptyGeneAnalysisResultException {
        return geneAnalysisService.analysisByDiseaseId(diseaseId, projection, interactors, SortBy.ENTITIES_PVALUE, OrderBy.ASC, Resource.TOTAL, pValue, includeDisease);
    }
}
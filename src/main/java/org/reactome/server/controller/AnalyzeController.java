package org.reactome.server.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.reactome.server.annotation.ParameterLogger;
import org.reactome.server.domain.analysis.AnalysisRequestData;
import org.reactome.server.domain.analysis.AnalysisResult;
import org.reactome.server.domain.analysis.Resource;
import org.reactome.server.domain.analysis.SortBy;
import org.reactome.server.exception.FailedAnalyzeDiseaseException;
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

    @ParameterLogger
    @ApiIgnore
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String analyse(@RequestBody AnalysisRequestData requestData) throws FailedAnalyzeDiseaseException {
        return geneAnalysisService.checkGeneListAnalysisResult(requestData);
    }

    @ApiIgnore
    @GetMapping
    public String apiDoc() {
        return "swagger/analysis-api-doc";
    }

    @ParameterLogger
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{disease}", produces = "application/json")
    @ApiOperation(value = "Do analysis by given a specific diseases and return the reacfoam result link.",
            notes = "Use projection to analyze the disease's associated identifiers retrieved from database over different species and project the result to Homo Sapiens(the projection is calculated by the orthologous slot in the Reactome database). " +
                    "Use interactors to decide if include interactors. " +
                    "the defines the pValue threshold. Only hit pathway with pValue equals or below the threshold will be returned. " +
                    "The includeDisease set to ‘false’ to exclude the disease pathways from the result (it does not alter the statistics) ",httpMethod = "GET", response = AnalysisResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "disease", value = "A specific disease", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "projection", value = "If project result to Homo Sapiens", dataType = "Boolean", paramType = "query", defaultValue = "true"),
            @ApiImplicitParam(name = "interactors", value = "Include interactors", dataType = "Boolean", paramType = "query", defaultValue = "true"),
            @ApiImplicitParam(name = "pValue", value = "defines the pValue threshold. Only hit pathway with pValue equals or below the threshold will be returned", dataType = "Number", paramType = "query", defaultValue = "1.0"),
            @ApiImplicitParam(name = "includeDisease", value = "set to ‘false’ to exclude the disease pathways from the result (it does not alter the statistics)", dataType = "Boolean", paramType = "query", defaultValue = "true")
    })
//    @ApiResponses({@ApiResponse(code = 406, message = "Failed to analyze a specific disease")})
    public @ResponseBody
    AnalysisResult diseaseAnalysis(@PathVariable(value = "disease") String disease,
                                   @RequestParam(value = "projection", required = false) Boolean projection,
                                   @RequestParam(value = "interactors", required = false) Boolean interactors,
                                   @RequestParam(value = "pValue", required = false) Float pValue,
                                   @RequestParam(value = "includeDisease", required = false) Boolean includeDisease) {
        return geneAnalysisService.analysisByDisease(disease, projection, interactors, SortBy.ENTITIES_PVALUE, OrderBy.ASC, Resource.TOTAL, pValue, includeDisease);
    }
}
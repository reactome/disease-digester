package org.reactome.server.controller;

import io.swagger.annotations.Api;
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
@SuppressWarnings("unused")
@Api(tags = "analyze", description = "Reactome DisGeNET analysis queries")
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

    @ParameterLogger
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{disease}", produces = "application/json")
    @ApiOperation(value = "Do analysis by a specific diseases and return the reacfoam result link.", httpMethod = "GET", response = AnalysisResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "disease", value = "A specific disease, identified by disease id, for example C4279912.", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "projection", value = "Map identifiers to Homo sapiens.", dataType = "Boolean", paramType = "query", defaultValue = "true"),
            @ApiImplicitParam(name = "interactors", value = "Include high confidence IntAct interactors to extend coverage of Reactome.", dataType = "Boolean", paramType = "query", defaultValue = "true"),
            @ApiImplicitParam(name = "pValue", value = "Defines the pValue threshold. Only hit pathways with pValue equals below the threshold will be returned.", dataType = "Number", paramType = "query", defaultValue = "1.0"),
            @ApiImplicitParam(name = "includeDiseasePathways", value = "Set to 'false' to exclude the Reactome disease pathways from the result (it does not alter the statistics).", dataType = "Boolean", paramType = "query", defaultValue = "true")
    })
//    @ApiResponses({@ApiResponse(code = 406, message = "Failed to analyze a specific disease id")})
    public @ResponseBody
    AnalysisResult diseaseAnalysis(@PathVariable(value = "disease") String disease,
                                   @RequestParam(value = "projection", required = false) Boolean projection,
                                   @RequestParam(value = "interactors", required = false) Boolean interactors,
                                   @RequestParam(value = "pValue", required = false) Float pValue,
                                   @RequestParam(value = "includeDiseasePathways", required = false) Boolean includeDiseasePathways) {
        return geneAnalysisService.analysisByDisease(disease, projection, interactors, SortBy.ENTITIES_PVALUE, OrderBy.ASC, Resource.TOTAL, pValue, includeDiseasePathways);
    }
}
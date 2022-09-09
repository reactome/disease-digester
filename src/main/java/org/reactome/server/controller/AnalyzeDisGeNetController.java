package org.reactome.server.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.reactome.server.annotation.ParameterLogger;
import org.reactome.server.domain.analysis.AnalysisRequestData;
import org.reactome.server.domain.analysis.AnalysisResult;
import org.reactome.server.domain.analysis.Resource;
import org.reactome.server.domain.analysis.SortBy;
import org.reactome.server.domain.model.SourceDatabase;
import org.reactome.server.exception.FailedAnalyzeDiseaseException;
import org.reactome.server.service.GeneAnalysisService;
import org.reactome.server.service.OrderBy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/disgenet/analyze")
@SuppressWarnings("unused")
@Tag(name = "analyze", description = "Reactome DisGeNET analysis queries")
public class AnalyzeDisGeNetController {

    private final GeneAnalysisService geneAnalysisService;

    public AnalyzeDisGeNetController(GeneAnalysisService geneAnalysisService) {
        this.geneAnalysisService = geneAnalysisService;
    }

    @ParameterLogger
    @Hidden
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String analyse(@RequestBody AnalysisRequestData requestData, HttpServletRequest request) throws FailedAnalyzeDiseaseException {
        return geneAnalysisService.checkGeneListAnalysisResult(requestData, UrlUtil.getBaseUrl(request));
    }

    @CrossOrigin
    @ParameterLogger
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{disease}", produces = "application/json")
    @Operation(summary = "Do analysis by a specific diseases and return the reacfoam result link.", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "406", description = "Failed to analyze a specific disease id")})
    public @ResponseBody
    AnalysisResult diseaseAnalysis(@Parameter(name = "disease", description = "A specific disease, identified by disease id.", required = true, example = "C4279912") @PathVariable(value = "disease") String disease,
                                   @Parameter(name = "projection", description = "Map identifiers to Homo sapiens.", example = "true") @RequestParam(value = "projection", required = false) Boolean projection,
                                   @Parameter(name = "interactors", description = "Include high confidence IntAct interactors to extend coverage of Reactome.", example = "true") @RequestParam(value = "interactors", required = false) Boolean interactors,
                                   @Parameter(name = "pValue", description = "Defines the pValue threshold. Only hit pathways with pValue equals below the threshold will be returned.", example = "1.0") @RequestParam(value = "pValue", required = false) Float pValue,
                                   @Parameter(name = "includeDiseasePathways", description = "Set to 'false' to exclude the Reactome disease pathways from the result (it does not alter the statistics).", example = "true") @RequestParam(value = "includeDiseasePathways", required = false) Boolean includeDiseasePathways,
                                   HttpServletRequest request) {
        return geneAnalysisService.analysisByDisease(disease, projection, interactors, SortBy.ENTITIES_PVALUE, OrderBy.ASC, Resource.TOTAL, pValue, includeDiseasePathways, SourceDatabase.DISGENET, UrlUtil.getBaseUrl(request));
    }
}
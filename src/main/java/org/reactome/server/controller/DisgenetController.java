package org.reactome.server.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.reactome.server.annotation.ParameterLogger;
import org.reactome.server.domain.DiseaseNameHintWord;
import org.reactome.server.domain.DiseaseResult;
import org.reactome.server.domain.GeneToDiseasesResult;
import org.reactome.server.service.DiseaseService;
import org.reactome.server.service.HintWordService;
import org.reactome.server.service.SortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "content", description = "Access Reactome imported DisGeNET content")
@Controller
@RequestMapping("/disgenet")
public class DisgenetController {

    private final DiseaseService diseaseService;
    private final HintWordService hintWordService;

    @Autowired
    public DisgenetController(DiseaseService diseaseService, HintWordService hintWordService) {
        this.diseaseService = diseaseService;
        this.hintWordService = hintWordService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/api")
    public String apiDoc() {
        return "swagger/disgenet-api-doc";
    }

    @ParameterLogger
    @GetMapping(value = "/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    @CrossOrigin()
    public @ResponseBody
    DiseaseResult findAll(
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(value = "geneSize", defaultValue = "10") Integer geneSize,
            @RequestParam(value = "score", defaultValue = "0") Float score,
            @RequestParam(value = "sort", defaultValue = "NAME") SortBy sortBy,
            @RequestParam(value = "order", defaultValue = "ASC") Sort.Direction order) {
        return diseaseService.findAll(pageNumber, pageSize, score, geneSize, sortBy, order);
    }

    @ParameterLogger
    @GetMapping(value = "/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    @CrossOrigin()
    public @ResponseBody
    DiseaseResult findByDiseaseName(@RequestParam("name") String diseaseName,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                    @RequestParam(required = false, defaultValue = "50") Integer pageSize,
                                    @RequestParam(required = false, defaultValue = "10") Integer geneSize,
                                    @RequestParam(required = false, defaultValue = "0") Float score,
                                    @RequestParam(required = false, defaultValue = "NAME") SortBy sort,
                                    @RequestParam(required = false, defaultValue = "ASC") Sort.Direction order) {
        return diseaseService.findByDiseaseName(pageNumber, pageSize, diseaseName, score, geneSize, sort, order);
    }


    /*TODO: store this value into user browser's cookie'*/
    @GetMapping(value = "/diseaseNameHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    @CrossOrigin()
    public @ResponseBody
    DiseaseNameHintWord getDiseaseNameHintWord() {
        return hintWordService.getDiseaseNameHintWord();
    }

    @GetMapping(value = "/getMaxGeneSize")
    @ResponseStatus(value = HttpStatus.OK)
    @CrossOrigin()
    public @ResponseBody
    Long getMaxGeneSize(@RequestParam("score") Float score, @RequestParam(value = "name", required = false) String name) {
        return Optional.ofNullable(diseaseService.getMaxGeneSize(score, name)).orElse(0L);
    }


    @Operation(summary = "Retrieve a detailed interaction information of a given accession")
    @RequestMapping(value = "/findByGenes", method = RequestMethod.POST, consumes = "text/plain", produces = "application/json")
    @ResponseBody
    @CrossOrigin()
    public GeneToDiseasesResult findByGenes(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Interactor accessions (or identifiers)",
                    required = true,
                    content = @Content(examples = {@ExampleObject("P04637, P01189")}))
            @RequestBody String geneAcs) {
        return new GeneToDiseasesResult("disgenet", diseaseService.findByGenes(
                Arrays.stream(geneAcs.split(",|;|\\n|\\t"))
                        .map(String::trim).collect(Collectors.toSet()
                        ))
        );
    }
}
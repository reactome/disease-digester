package org.reactome.server.controller;

import org.reactome.server.annotation.ParameterLogger;
import org.reactome.server.domain.DiseaseItemResult;
import org.reactome.server.domain.DiseaseNameHintWord;
import org.reactome.server.service.DiseaseItemService;
import org.reactome.server.service.HintWordService;
import org.reactome.server.service.OrderBy;
import org.reactome.server.service.SortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@ApiIgnore
@Controller
@RequestMapping("/disgenet")
public class DisgenetController {

    private final DiseaseItemService diseaseItemService;
    private final HintWordService hintWordService;

    @Autowired
    public DisgenetController(DiseaseItemService diseaseItemService, HintWordService hintWordService) {
        this.diseaseItemService = diseaseItemService;
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
    public @ResponseBody
    DiseaseItemResult findAll(
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(value = "geneSize", defaultValue = "10") Integer geneSize,
            @RequestParam(value = "score", defaultValue = "0") Float score,
            @RequestParam(value = "sort", defaultValue = "NAME") SortBy sortBy,
            @RequestParam(value = "order", defaultValue = "ASC") OrderBy orderBy) {
        return diseaseItemService.findAll(pageNumber, pageSize, score, geneSize, sortBy, orderBy);
    }

    @ParameterLogger
    @GetMapping(value = "/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    DiseaseItemResult findByDiseaseName(@RequestParam("name") String diseaseName,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                        @RequestParam(required = false, defaultValue = "50") Integer pageSize,
                                        @RequestParam(required = false, defaultValue = "10") Integer geneSize,
                                        @RequestParam(required = false, defaultValue = "0") Float score,
                                        @RequestParam(required = false, defaultValue = "NAME") SortBy sort,
                                        @RequestParam(required = false, defaultValue = "ASC") OrderBy order) {
        return diseaseItemService.findByDiseaseName(pageNumber, pageSize, diseaseName, score, geneSize, sort, order);
    }


    /*TODO: store this value into user browser's cookie'*/
    @GetMapping(value = "/diseaseNameHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    DiseaseNameHintWord getDiseaseNameHintWord() {
        return hintWordService.getDiseaseNameHintWord();
    }

    @GetMapping(value = "/getMaxGeneSize")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Integer getMaxGeneSize(@RequestParam("score") Float score, @RequestParam(value = "name", required = false) String name) {
        return Optional.ofNullable(diseaseItemService.getMaxGeneSize(score, name)).orElse(0);
    }
}
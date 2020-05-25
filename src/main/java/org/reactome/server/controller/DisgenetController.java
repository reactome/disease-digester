package org.reactome.server.controller;

import org.reactome.server.domain.DiseaseItemResult;
import org.reactome.server.domain.DiseaseNameHintWord;
import org.reactome.server.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/disgenet")
public class DisgenetController {

    private static final Logger logger = LoggerFactory.getLogger(DisgenetController.class);
    private DiseaseItemService diseaseItemService;
    private HintWordService hintWordService;

    @Autowired
    public DisgenetController(DiseaseItemService diseaseItemService, HintWordService hintWordService) {
        this.diseaseItemService = diseaseItemService;
        this.hintWordService = hintWordService;
    }

    @GetMapping
    public String index() {
        return "index";
    }


    @GetMapping(value = "/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    DiseaseItemResult findAll(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("geneSize") Integer geneSize,
            @RequestParam("score") Float score,
            @RequestParam("sort") SortBy sortBy,
            @RequestParam("order") OrderBy orderBy) {
        return diseaseItemService.findAll(pageNumber, pageSize, score, geneSize, sortBy, orderBy);
    }

    @GetMapping(value = "/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    DiseaseItemResult findByDiseaseName(@RequestParam("name") String diseaseName,
                                        @RequestParam("pageNumber") Integer pageNumber,
                                        @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam("geneSize") Integer geneSize,
                                        @RequestParam("score") Float score,
                                        @RequestParam("sort") SortBy sortBy,
                                        @RequestParam("order") OrderBy orderBy) {
        return diseaseItemService.findByDiseaseName(pageNumber, pageSize, diseaseName, score, geneSize, sortBy, orderBy);
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
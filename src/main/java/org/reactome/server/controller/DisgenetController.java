package org.reactome.server.controller;

import org.reactome.server.domain.AnalysisRequestData;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.HintWord;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.reactome.server.service.DiseaseItemService;
import org.reactome.server.service.GeneAnalysisService;
import org.reactome.server.service.HintWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/disgenet")
public class DisgenetController {

    private static final Logger logger = LoggerFactory.getLogger(DisgenetController.class);
    private DiseaseItemService diseaseItemService;
    private GeneAnalysisService geneAnalysisService;
    private HintWordService hintWordService;

    @Autowired
    public DisgenetController(DiseaseItemService diseaseItemService, GeneAnalysisService geneAnalysisService, HintWordService hintWordService) {
        this.diseaseItemService = diseaseItemService;
        this.geneAnalysisService = geneAnalysisService;
        this.hintWordService = hintWordService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/{diseaseId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    String diseaseAnalysis(@PathVariable("diseaseId") String diseaseId) throws EmptyGeneAnalysisResultException {
//        return new ModelAndView(new RedirectView(geneAnalysisService.analysisByDiseaseName(diseaseId)));
        return geneAnalysisService.analysisByDiseaseName(diseaseId);
    }

    @GetMapping(value = "/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findAll(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("geneSize") Integer geneSize,
            @RequestParam(value = "cutoffValue", required = false) Float cutoffValue,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findAll(pageNumber, pageSize, geneSize, cutoffValue, sortBy, orderBy);
    }

    @GetMapping(value = "/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findByDiseaseName(@RequestParam("name") String diseaseName,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @RequestParam(value = "geneSize", required = false) Integer geneSize,
                                        @RequestParam(value = "cutoffValue", required = false) Float cutoffValue,
                                        @RequestParam(value = "sort", required = false) String sortBy,
                                        @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findByDiseaseName(diseaseName, pageNumber, pageSize, geneSize, cutoffValue, sortBy, orderBy);
    }

    @PostMapping(value = "/analyze")
    @ResponseStatus(value = HttpStatus.OK)
//    public ModelAndView showPathway(@RequestBody AnalysisGeneList geneList) throws EmptyGeneAnalysisResultException {
    public @ResponseBody
    String showPathway(@RequestBody AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException {
//        return new ModelAndView(new RedirectView(geneAnalysisService.checkGeneListAnalysisResult(geneList)));
        return geneAnalysisService.checkGeneListAnalysisResult(requestData);
    }

    @GetMapping(value = "/diseaseNameHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    HintWord getDiseaseNameHintWord() {
        return hintWordService.getDiseaseNameHintWord();
    }

    @GetMapping(value = "/getMaxGeneSize")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Integer getMaxGeneSize() {
        return diseaseItemService.getMaxGeneSize();
    }
}
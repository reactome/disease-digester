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
public class DiseaseDigesterController {

    private static final Logger logger = LoggerFactory.getLogger(DiseaseDigesterController.class);
    private DiseaseItemService diseaseItemService;
    private GeneAnalysisService geneAnalysisService;
    private HintWordService hintWordService;

    @Autowired
    public DiseaseDigesterController(DiseaseItemService diseaseItemService, GeneAnalysisService geneAnalysisService, HintWordService hintWordService) {
        this.diseaseItemService = diseaseItemService;
        this.geneAnalysisService = geneAnalysisService;
        this.hintWordService = hintWordService;
    }

//    @GetMapping(value = "/{blackHole}")
//    @ResponseStatus(value = HttpStatus.OK)
//    public @ResponseBody
//    String spit(@PathVariable String blackHole) {
//        logger.info("catch unknown guy: " + blackHole);
//        return "Alien: " + blackHole;
//    }

    @RequestMapping(value = "/disgenet", method = RequestMethod.GET)
    public String disgenet() {
        // When we load the schema page, DatabaseObject is loaded by default, then we redirect to it
        return "index";
    }

    @GetMapping(value = "/disgenet/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findAll(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("geneSize") Integer geneSize,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findAll(pageNumber, pageSize, geneSize, sortBy, orderBy);
    }

    @GetMapping(value = "/disgenet/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findByDiseaseName(@RequestParam("name") String diseaseName,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @RequestParam(value = "geneSize", required = false) Integer geneSize,
                                        @RequestParam(value = "sort", required = false) String sortBy,
                                        @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findByDiseaseName(diseaseName, pageNumber, pageSize, geneSize, sortBy, orderBy);
    }

    @GetMapping(value = "/disgenet/findByDiseaseClass")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findByDiseaseClass(@RequestParam("class") String diseaseClass,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @RequestParam(value = "geneSize", required = false) Integer geneSize,
                                         @RequestParam(value = "sort", required = false) String sortBy,
                                         @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findByDiseaseClass(diseaseClass, pageNumber, pageSize, geneSize, sortBy, orderBy);
    }

    @PostMapping(value = "/disgenet/analyze")
    @ResponseStatus(value = HttpStatus.OK)
//    public ModelAndView showPathway(@RequestBody AnalysisGeneList geneList) throws EmptyGeneAnalysisResultException {
    public @ResponseBody
    String showPathway(@RequestBody AnalysisRequestData requestData) throws EmptyGeneAnalysisResultException {
//        return new ModelAndView(new RedirectView(geneAnalysisService.checkGeneListAnalysisResult(geneList)));
        return geneAnalysisService.checkGeneListAnalysisResult(requestData);
    }

    @GetMapping(value = "/disgenet/diseaseNameHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    HintWord getDiseaseNameHintWord() {
        return hintWordService.getDiseaseNameHintWord();
    }

    @GetMapping(value = "/disgenet/diseaseClassHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    HintWord getDiseaseClassHintWord() {
        return hintWordService.getDiseaseClassHintWord();
    }

    @GetMapping(value = "/disgenet/getMaxGeneSize")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Integer getMaxGeneSize() {
        return diseaseItemService.getMaxGeneSize();
    }
}
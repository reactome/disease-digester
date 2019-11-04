package org.reactome.server.controller;

import org.reactome.server.domain.AnalysisGeneList;
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
import org.springframework.web.bind.annotation.*;

@RestController
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

    @GetMapping(value = "/{blackHole}")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String spit(@PathVariable String blackHole) {
        logger.info("catch unknown guy: " + blackHole);
        return "Alien: " + blackHole;
    }

    @GetMapping(value = "/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findAll(page, size, sortBy, orderBy);
    }

    @GetMapping(value = "/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findByDiseaseName(@RequestParam("name") String diseaseName,
                                        @RequestParam(value = "page", required = false) Integer pageNumber,
                                        @RequestParam(value = "size", required = false) Integer pageSize,
                                        @RequestParam(value = "sort", required = false) String sortBy,
                                        @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findByDiseaseName(diseaseName, pageNumber, pageSize, sortBy, orderBy);
    }

    @GetMapping(value = "/findByDiseaseClass")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    Page<DiseaseItem> findByDiseaseClass(@RequestParam("class") String diseaseClass,
                                         @RequestParam(value = "page", required = false) Integer pageNumber,
                                         @RequestParam(value = "size", required = false) Integer pageSize,
                                         @RequestParam(value = "sort", required = false) String sortBy,
                                         @RequestParam(value = "order", required = false) String orderBy) {
        return diseaseItemService.findByDiseaseClass(diseaseClass, pageNumber, pageSize, sortBy, orderBy);
    }

    @PostMapping(value = "/analyze")
    @ResponseStatus(value = HttpStatus.OK)
//    public ModelAndView showPathway(@RequestBody AnalysisGeneList geneList) throws EmptyGeneAnalysisResultException {
    public @ResponseBody
    String showPathway(@RequestBody AnalysisGeneList geneList) throws EmptyGeneAnalysisResultException {
//        return new ModelAndView(new RedirectView(geneAnalysisService.checkGeneListAnalysisResult(geneList)));
        return geneAnalysisService.checkGeneListAnalysisResult(geneList);
    }

    @GetMapping(value = "/diseaseNameHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    HintWord getDiseaseNameHintWord() {
        return hintWordService.getDiseaseNameHintWord();
    }

    @GetMapping(value = "/diseaseClassHintWord")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    HintWord getDiseaseClassHintWord() {
        return hintWordService.getDiseaseClassHintWord();
    }
}
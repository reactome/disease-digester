package org.reactome.server.controller;

import org.reactome.server.domain.PaginationResult;
import org.reactome.server.service.DiseaseItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class DiseaseDigesterController {

    private static final Logger logger = LoggerFactory.getLogger(DiseaseDigesterController.class);
    private DiseaseItemService diseaseItemService;

    @Autowired
    public DiseaseDigesterController(DiseaseItemService diseaseItemService) {
        this.diseaseItemService = diseaseItemService;
    }

    @GetMapping(value = "/{blackHole}")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String spit(@PathVariable String blackHole) {
        logger.info("catch unknown guy: " + blackHole);
        return "Alien: " + blackHole;
    }

    @GetMapping(value = "/findByDiseaseName")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PaginationResult findByDiseaseName(@RequestParam("diseaseName") String diseaseName,
                                       @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                       @RequestParam(value = "sortBy", required = false) String sortBy,
                                       @RequestParam(value = "orderBy", required = false) String orderBy) {
        return diseaseItemService.findDiseaseItemsByDiseaseName(diseaseName, pageNumber, pageSize, sortBy, orderBy);
    }

    @GetMapping(value = "/findByDiseaseClass")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PaginationResult findByDiseaseClass(@RequestParam("diseaseClass") String diseaseClass,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                        @RequestParam(value = "orderBy", required = false) String orderBy) {
        return diseaseItemService.findDiseaseItemsByDiseaseClass(diseaseClass, pageNumber, pageSize, sortBy, orderBy);
    }

    @GetMapping(value = "/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PaginationResult findAll(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "orderBy", required = false) String orderBy) {

        return diseaseItemService.findAll(pageNumber, pageSize, sortBy, orderBy);
    }
}
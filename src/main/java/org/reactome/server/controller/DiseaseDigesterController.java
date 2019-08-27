package org.reactome.server.controller;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;
import org.reactome.server.domain.PaginationResult;
import org.reactome.server.service.DiseaseItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class DiseaseDigesterController {

    private static final Logger logger = LoggerFactory.getLogger(DiseaseDigesterController.class);
    private final DiseaseItemService diseaseItemService;

    public DiseaseDigesterController(DiseaseItemService diseaseItemService) {
        this.diseaseItemService = diseaseItemService;
    }

    @GetMapping(value = "/test/{echo}")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    String echo(@PathVariable String echo) {
        logger.info("start to log something...");
        return echo;
    }

    @GetMapping(value = "/test/findAll")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<DiseaseItem> findAll() {
        List<DiseaseItem> diseaseItems = diseaseItemService.findAll();
        logger.info(diseaseItems.toString());
        return diseaseItems;
    }

    @GetMapping(value = "/test/saveOne")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    DiseaseItem saveOne() {
        DiseaseItem diseaseItem = new DiseaseItem();
        diseaseItem.setDiseaseClass("test");
        diseaseItem.setDiseaseId("test");
        diseaseItem.setDiseaseName("test");
        diseaseItem.setGeneItems(Collections.singletonList(new GeneItem("demo", "demo", "demo", diseaseItem)));
        return diseaseItemService.save(diseaseItem);
    }

    @GetMapping(value = "/test/pagination")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    PaginationResult pagination(@RequestParam("pageNumber") Integer pageNumber, @RequestParam(value = "pageSize") Integer pageSize) {
        return diseaseItemService.getPaginationResult(pageNumber, pageSize);
    }
}
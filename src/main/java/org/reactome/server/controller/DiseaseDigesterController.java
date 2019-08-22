package org.reactome.server.controller;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.service.DiseaseItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/test/findAll", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    List<DiseaseItem> findAll() {
        List<DiseaseItem> diseaseItems = diseaseItemService.findAll();
        logger.info(diseaseItems.toString());
        return diseaseItems;
    }
}

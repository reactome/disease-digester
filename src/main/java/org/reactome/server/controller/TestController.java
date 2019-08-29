package org.reactome.server.controller;

import org.reactome.server.domain.GeneItem;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.reactome.server.service.DiseaseItemService;
import org.reactome.server.service.GeneAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    /*
    this controller has been created for jsp page test only
     */
    private final DiseaseItemService diseaseItemService;
    private GeneAnalysisService geneAnalysisService;

    public @Autowired
    TestController(DiseaseItemService diseaseItemService, GeneAnalysisService geneAnalysisService) {
        this.diseaseItemService = diseaseItemService;
        this.geneAnalysisService = geneAnalysisService;
    }

    @GetMapping(value = "/pagination")
    public String test(
            @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "orderBy", required = false) String orderBy, Model model) {
//        return diseaseItemService.findAll(pageNumber, pageSize, sortBy, orderBy);
        model.addAttribute("page", diseaseItemService.findAll(pageNumber, pageSize, sortBy, orderBy));
        return "test";
    }

    @PostMapping("/analyze")
    public ModelAndView showPathway(@RequestBody List<GeneItem> geneItems) throws EmptyGeneAnalysisResultException {
        return new ModelAndView(new RedirectView(geneAnalysisService.checkGeneListAnalysisResult(geneItems)));
    }
}

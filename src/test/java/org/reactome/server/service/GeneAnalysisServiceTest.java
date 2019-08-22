package org.reactome.server.service;

import org.junit.Test;
import org.reactome.server.TestBase;
import org.reactome.server.exception.EmptyGeneAnalysisResultException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

//import org.reactome.server.config.DiseaseDigesterJpaConfig;


public class GeneAnalysisServiceTest extends TestBase {

    @Autowired
    private GeneAnalysisService geneAnalysisService;


    @Test
    public void checkGeneAnalysisResultTest() {
//        ArrayList<String> geneList = new ArrayList<String>() {{
//            add("pten");
//            add("p53");
//        }};
//        try {
//            System.out.println(geneAnalysisService.checkGeneAnalysisResult(geneList));
//        } catch (EmptyGeneAnalysisResultException e) {
//            e.printStackTrace();
//        }
    }
}
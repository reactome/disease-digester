package org.reactome.server.service;

import org.junit.Test;
import org.reactome.server.TestBase;
import org.springframework.beans.factory.annotation.Autowired;


public class DiseaseItemServiceTest extends TestBase {

    @Autowired
    DiseaseItemService diseaseItemService;

    @Test
    public void saveAllTest() throws Exception {
//        diseaseItemService.saveAll(getDiseaseItemsFromFile("src/test/resources/curated_gene_disease_associations.tsv"));
    }
}
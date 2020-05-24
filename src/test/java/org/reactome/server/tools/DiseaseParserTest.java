package org.reactome.server.tools;


import org.junit.Assert;
import org.junit.Test;
import org.reactome.server.TestBase;
import org.reactome.server.domain.DiseaseItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class DiseaseParserTest extends TestBase {
    @Test
    public void loadDiseaseItemsTest() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream("src/test/resources/curated_gene_disease_associations.tsv.gz")), StandardCharsets.UTF_8));
        List<DiseaseItem> diseaseItems = new DiseaseParser(bufferedReader).getDiseaseItems();
//        diseaseItems.forEach(System.out::println);
        Assert.assertEquals(10370, diseaseItems.size());
    }
}

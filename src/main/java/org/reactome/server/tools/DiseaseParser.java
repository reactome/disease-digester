package org.reactome.server.tools;

import org.apache.commons.lang3.StringUtils;
import org.reactome.server.domain.DataRow;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class DiseaseParser {
    /*
    This disease parser is hard-coded correspond to the tsv file header name
     */

    private List<DiseaseItem> diseaseItems;
    private static final String DELIMITER = "\t";
    private final InputStream GENEID_4_UNIPROT = this.getClass().getClassLoader().getResourceAsStream("geneid_4_uniprot.tsv");
//    private final InputStream DISEASE_CLASS = this.getClass().getClassLoader().getResourceAsStream("disease-class.properties");

    DiseaseParser(BufferedReader file) throws Exception {
        setDiseaseItems(loadDiseaseItems(file));
    }

    List<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    private void setDiseaseItems(List<DiseaseItem> diseaseItems) {
        this.diseaseItems = diseaseItems;
    }

    private List<DiseaseItem> loadDiseaseItems(BufferedReader file) {
        List<String> table = new ArrayList<>();
        try {
            file.readLine();// Jump the header line.
            String line;
            while ((line = file.readLine()) != null) table.add(line);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transferTable2DiseaseItemList(table);
    }

    private List<DiseaseItem> transferTable2DiseaseItemList(List<String> table) {
        /* transfer Map<table> to DiseaseItem objects */
        List<DataRow> dataRowList = table.stream().map((String s) -> {
            String[] ss = s.split(DELIMITER);
            return new DataRow(ss[0].trim(), ss[1].trim(), ss[4].trim(), trimDiseaseName(ss[5].trim()), Float.parseFloat(ss[9].trim()));
        }).collect(toList());
        Map<DiseaseItem, List<DataRow>> diseaseItemMap = dataRowList.stream().collect(Collectors.groupingBy((dataRow -> new DiseaseItem(dataRow.getDiseaseId(), dataRow.getDiseaseName()))));
        return diseaseItemMap.entrySet().stream().map(entry -> {
            DiseaseItem diseaseItem = entry.getKey();
            List<GeneItem> geneItems = entry.getValue().stream().map(dataRow -> new GeneItem(dataRow.getGeneId(), dataRow.getDiseaseId(), dataRow.getGeneSymbol(), dataRow.getScore())).collect(toList());
            diseaseItem.setGeneItems(geneItems);
            return diseaseItem;
        }).collect(toList());
    }

    /**
     * trim disease name: replace all non-word character with underscore, trim and capitalize
     *
     * @param diseaseName
     * @return trimmed disease name
     */
    private String trimDiseaseName(String diseaseName) {
        return Arrays.stream(diseaseName.split("[^\\w]"))
                .filter(name -> name.matches("\\w+"))
                .map(String::trim)
                .map(String::toLowerCase)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining("_"));
    }

    private Map<String, String> loadGeneId2AccNumMap() {
        /* load gene id to UniProtKB mapping pairs */
        Map<String, String> geneIdMap = new HashMap<>();
        BufferedReader TSVFile = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GENEID_4_UNIPROT)));
        String DELIMITER = "\t";
        try {
            TSVFile.readLine(); // Read header line.
            String line = TSVFile.readLine(); // Read first data row.
            while (line != null) {
                String[] split = line.split(DELIMITER);
                assert 2 == split.length;
                geneIdMap.put(split[1], split[0]);
                line = TSVFile.readLine();
            }
            TSVFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return geneIdMap;
    }

//    private Map<String, String> loadDiseaseClass2TopDiseaseClassMap() {
//        /* load disease class abbreviation name to explicit name mapping paris */
//        Properties properties = new Properties();
//        try {
//            properties.load(new InputStreamReader(Objects.requireNonNull(DISEASE_CLASS)));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return properties.entrySet().stream().collect(toMap(e -> e.getKey().toString().trim(), e -> e.getValue().toString().trim()));
//    }
}
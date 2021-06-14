package org.reactome.server.tools;

import org.reactome.server.domain.DataRow;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class DiseaseParser {
    /*
    This disease parser is hard-coded correspond to the tsv file
     */

    private List<DiseaseItem> diseaseItems;
    private static final String DELIMITER = "\t";
    private final InputStream GENEID_4_UNIPROT = this.getClass().getClassLoader().getResourceAsStream("geneid_to_uniprot.tsv");
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
        /*Read raw text data from file*/
        List<String> table = new ArrayList<>();
        try {
            /*Jump the header line.*/
            file.readLine();
            String line;
            while ((line = file.readLine()) != null) table.add(line);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Wrapping text data as DataRow object*/
        List<DataRow> dataRowList = table.stream().map((String s) -> {
            String[] ss = s.split(DELIMITER);
            return new DataRow(ss[0].trim(), ss[1].trim(), ss[4].trim(), trimDiseaseName(ss[5].trim()), Float.parseFloat(ss[9].trim()));
        }).collect(Collectors.toList());
        return convertDataRowList2DiseaseItemList(dataRowList);
    }

    private List<DiseaseItem> convertDataRowList2DiseaseItemList(List<DataRow> dataRowList) {
        /*Grouping DataRow by diseaseId*/
        Map<DiseaseItem, List<DataRow>> diseaseItemMap = dataRowList.stream().collect(Collectors.groupingBy((dataRow -> new DiseaseItem(dataRow.getDiseaseId(), dataRow.getDiseaseName()))));
        assert diseaseItemMap.size() < dataRowList.size();

        Map<String, String> geneId2AccNumMap = loadGeneId2AccNumMap();

        /*Convert DataRow into DiseaseItem*/
        List<DiseaseItem> diseaseItemList = diseaseItemMap.entrySet().stream().map(entry -> {
            DiseaseItem diseaseItem = entry.getKey();
            List<GeneItem> geneItems = entry.getValue().stream().map(dataRow -> new GeneItem(dataRow.getGeneId(), dataRow.getGeneSymbol(), geneId2AccNumMap.get(dataRow.getGeneId()), dataRow.getScore())).collect(Collectors.toList());
            diseaseItem.setGeneItems(geneItems);
            return diseaseItem;
        }).collect(Collectors.toList());
        // TODO: 2020/6/12 for data version 7.0, the assert is:
        assert 2037 == diseaseItemList.stream().max(Comparator.comparing(diseaseItem -> diseaseItem.getGeneItems().size())).get().getGeneItems().size();
        assert "C3714756".equals(diseaseItemList.stream().max(Comparator.comparing(diseaseItem -> diseaseItem.getGeneItems().size())).get().getDiseaseId());
        return diseaseItemList;
    }

    /**
     * trim disease name: replace all non-word character with underscore, trim and capitalize
     *
     * @param diseaseName given the disease name.
     * @return trimmed disease name
     */
    private String trimDiseaseName(String diseaseName) {
        return Arrays.stream(diseaseName.split("[^\\w]"))
                .filter(name -> name.matches("\\w+"))
                .map(String::trim)
                .collect(Collectors.joining(" "));
    }

    private Map<String, String> loadGeneId2AccNumMap() {
        /* Load GeneId to UniProtKB mapping pairs */
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
}
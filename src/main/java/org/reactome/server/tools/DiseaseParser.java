package org.reactome.server.tools;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

class DiseaseParser {
    /*
    This disease parser is hard-coded correspond to the tsv file header name
     */

    private List<DiseaseItem> diseaseItems;
    private static final String DELIMITER = "\t";
    private final InputStream GENEID_4_UNIPROT = this.getClass().getClassLoader().getResourceAsStream("geneid_4_uniprot.tsv");
    private final InputStream DISEASE_CLASS = this.getClass().getClassLoader().getResourceAsStream("disease-class.properties");

    DiseaseParser(BufferedReader file) throws Exception {
        setDiseaseItems(loadDiseaseItems(file));
    }

    List<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    private void setDiseaseItems(List<DiseaseItem> diseaseItems) {
        this.diseaseItems = diseaseItems;
    }

    private List<DiseaseItem> loadDiseaseItems(BufferedReader file) throws Exception {
//      <-- read table header start -->
        String headerLine = file.readLine(); // Read header line.
        List<String> headers = Arrays.asList(Objects.requireNonNull(headerLine.split(DELIMITER)));
//      <-- read table header end -->

        List<Map<String, String>> table = new ArrayList<>();


//      <-- read content start -->
        String line = file.readLine(); // Read first data row.
        while (line != null) {
            List<String> rowData = Arrays.asList(line.split(DELIMITER));
            Map<String, String> newRow = new LinkedHashMap<>();
            assert headers.size() == rowData.size();
            for (int i = 0; i < headers.size(); i++) {
                newRow.put(headers.get(i), rowData.get(i).trim());
            }
            table.add(newRow);
            line = file.readLine(); // Read next line of data.
        }
        //      <-- read content end -->
        file.close();
//        return cleavage(transferTable2DiseaseItems(table));
        return abbreviatedAttributeMapping(transferTable2DiseaseItems(table));
    }

    private List<DiseaseItem> transferTable2DiseaseItems(List<Map<String, String>> table) {
        /* transfer Map<table> to DiseaseItem objects */
        List<String> diseaseFields = Arrays.stream(DiseaseItem.class.getDeclaredFields())
                .map(Field::getName)
                .filter(s -> s.contains("disease"))
                .collect(toList());
        List<String> geneFields = Arrays.stream(GeneItem.class.getDeclaredFields())
                .map(Field::getName)
                .filter(s -> s.contains("gene"))
                .collect(toList());

        /* grouping disease with disease id */
        Map<Map<String, String>, List<String>> byDiseaseId = table.stream()
                .collect(Collectors.groupingBy(m -> diseaseFields.stream()
                        .collect(toMap(Function.identity(), m::get)), mapping(n -> n.get("geneId"), toList())));

        /* grouping gene with gene id */
        Map<Map<String, String>, List<String>> byGeneId = table.stream()
                .collect(Collectors.groupingBy(m -> geneFields.stream()
                        .collect(toMap(Function.identity(), m::get)), mapping(n -> n.get("diseaseId"), toList())));

        /* record mapping relationship */
        Map<String, List<String>> diseaseIdMap = new HashMap<>();
        byDiseaseId.forEach((k, v) -> diseaseIdMap.put(k.get("diseaseId"), v));
        Map<String, List<String>> geneIdMap = new HashMap<>();
        byGeneId.forEach((k, v) -> geneIdMap.put(k.get("geneId"), v));

        List<DiseaseItem> diseaseItems = new ArrayList<>();
        List<GeneItem> geneItems = new ArrayList<>();

        byDiseaseId.keySet().forEach(m -> diseaseItems.add(new DiseaseItem(m.get("diseaseId"), m.get("diseaseName").replaceAll("[^\\w]+", "_"), m.get("diseaseClass"))));

        List<DiseaseItem> uniqueDiseaseItems = cleavageDiseaseItemsByDiseaseClass(diseaseItems);

        byGeneId.keySet().forEach(m -> geneItems.add(new GeneItem(m.get("geneId"), m.get("geneSymbol"))));

//        geneItems.forEach(geneItem -> geneItem.setDiseaseItems(uniqueDiseaseItems.stream().filter(diseaseItem -> geneIdMap.get(geneItem.getGeneId()).contains(diseaseItem.getDiseaseId())).collect(toList())));
        Collections.synchronizedList(geneItems).parallelStream().forEach(geneItem -> geneItem.setDiseaseItems(uniqueDiseaseItems.stream().filter(diseaseItem -> geneIdMap.get(geneItem.getGeneId()).contains(diseaseItem.getDiseaseId())).collect(toSet())));

//        uniqueDiseaseItems.forEach(diseaseItem -> diseaseItem.setGeneItems(geneItems.stream().filter(geneItem -> diseaseIdMap.get(diseaseItem.getDiseaseId()).contains(geneItem.getGeneId())).collect(toList())));
        Collections.synchronizedList(uniqueDiseaseItems).parallelStream().forEach(diseaseItem -> diseaseItem.setGeneItems(geneItems.stream().filter(geneItem -> diseaseIdMap.get(diseaseItem.getDiseaseId()).contains(geneItem.getGeneId())).collect(toList())));

        return uniqueDiseaseItems;
    }


    private List<DiseaseItem> abbreviatedAttributeMapping(List<DiseaseItem> diseaseItems) {
        Map<String, String> diseaseClassMap = loadDiseaseClass2TopDiseaseClassMap();
        Map<String, String> geneId2AccNumMap = loadGeneId2AccNumMap();

        Collections.synchronizedList(diseaseItems).parallelStream().forEach(diseaseItem -> diseaseItem.setDiseaseClass(diseaseClassMap.get(diseaseItem.getDiseaseClass())));
        Collections.synchronizedList(diseaseItems).parallelStream().forEach(
                diseaseItem -> diseaseItem.getGeneItems().forEach(
                        geneItem -> geneItem.setAccessionNumber(geneId2AccNumMap.get(geneItem.getGeneId()))));
        return diseaseItems;
    }

    private List<DiseaseItem> cleavageDiseaseItemsByDiseaseClass(List<DiseaseItem> diseaseItems) {
        List<DiseaseItem> hasMoreThanOneDiseaseClass = diseaseItems.stream().filter(diseaseItem -> diseaseItem.getDiseaseClass().contains(";")).collect(toList());
        diseaseItems.removeAll(hasMoreThanOneDiseaseClass);
        diseaseItems.addAll(hasMoreThanOneDiseaseClass.stream().flatMap(diseaseItem -> new DiseaseItem().cleavage(diseaseItem).stream()).collect(toList()));
        return diseaseItems;
    }

    private Map<String, String> loadGeneId2AccNumMap() {
        /* load gene id ot UniProtKB mapping paris */
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

    private Map<String, String> loadDiseaseClass2TopDiseaseClassMap() {
        /* load disease class abbreviation name to explicit name mapping paris */
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(DISEASE_CLASS)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.entrySet().stream().collect(toMap(e -> e.getKey().toString().trim(), e -> e.getValue().toString().trim()));
    }
}
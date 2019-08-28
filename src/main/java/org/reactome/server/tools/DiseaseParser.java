package org.reactome.server.tools;

import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.*;
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

    private final InputStream GENEID_4_UNIPROT = this.getClass().getClassLoader().getResourceAsStream("geneid_4_uniprot.tsv");
    private final InputStream DISEASE_CLASS = this.getClass().getClassLoader().getResourceAsStream("disease-class.properties");

    DiseaseParser(JSAPResult config) throws Exception {
        setDiseaseItems(loadDiseaseItemsFromFile(config));
    }

    List<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    private void setDiseaseItems(List<DiseaseItem> diseaseItems) {
        this.diseaseItems = diseaseItems;
    }

    private List<DiseaseItem> loadDiseaseItemsFromFile(JSAPResult config) throws Exception {
        return loadDiseaseItemsFromFile(config.getString("fileName"));
    }

    private List<DiseaseItem> loadDiseaseItemsFromFile(String fileName) throws Exception {

        BufferedReader TSVFile = new BufferedReader(new FileReader(fileName));
        String delimiter = fileName.endsWith(".tsv") ? "\t" : ",";

//      <-- read table header start -->
        String headerLine = TSVFile.readLine(); // Read header line.
        List<String> headers = Arrays.asList(Objects.requireNonNull(headerLine.split(delimiter)));
//      <-- read table header end -->

        List<Map<String, String>> table = new ArrayList<>();


//      <-- read content start -->
        String line = TSVFile.readLine(); // Read first data row.
        while (line != null) {
            List<String> rowData = Arrays.asList(line.split(delimiter));
            Map<String, String> newRow = new LinkedHashMap<>();
            assert headers.size() == rowData.size();
            for (int i = 0; i < headers.size(); i++) {
                newRow.put(headers.get(i), rowData.get(i).trim());
            }
            table.add(newRow);
            line = TSVFile.readLine(); // Read next line of data.
        }
        //      <-- read content end -->
        TSVFile.close();
        return cleavage(transferTable2DiseaseItems(table));
    }

    private List<DiseaseItem> transferTable2DiseaseItems(List<Map<String, String>> table) throws IOException {
        /* transfer Map<table> to DiseaseItem objects */
        List<String> diseaseFields = Arrays.stream(DiseaseItem.class.getDeclaredFields())
                .map(Field::getName)
                .filter(s -> s.contains("disease"))
                .collect(toList());
        List<String> geneFields = Arrays.stream(GeneItem.class.getDeclaredFields())
                .map(Field::getName)
                .filter(s -> s.contains("gene"))
                .collect(toList());

        // grouping with disease name
        Map<Map<String, String>, List<Map<String, String>>> byDiseaseName = table.stream()
                .collect(Collectors.groupingBy((Map<String, String> m) -> diseaseFields.stream()
                                .collect(toMap(Function.identity(), m::get)),
                        mapping((Map<String, String> n) -> geneFields.stream()
                                .collect(toMap(Function.identity(), n::get)), toList())));

        // create new object according to the grouped attributes
        List<DiseaseItem> diseaseItems = new ArrayList<>();
        Map<String, String> geneId2AccNumMap = loadGeneId2AccNumMap(GENEID_4_UNIPROT);
        byDiseaseName.forEach((key, value) -> {
            DiseaseItem diseaseItem = new DiseaseItem(key.get("diseaseId"), key.get("diseaseName").replaceAll("[^\\w]+", "_"), key.get("diseaseClass"));
            List<GeneItem> geneItems = value.stream()
                    .map(g -> new GeneItem(g.get("geneId"), g.get("geneSymbol"),
                            geneId2AccNumMap.get(g.get("geneId"))))
                    .sorted(Comparator.comparing(GeneItem::getGeneSymbol))
                    .collect(toList());
            diseaseItem.setGeneItems(geneItems);
            diseaseItems.add(diseaseItem);
        });
        return diseaseItems;
    }


//    private List<DiseaseItem> cleavageDiseaseClass(List<DiseaseItem> diseaseItems) {
//        /* duplicate/cleavage object who has more than one disease classes and do disease class name substitute*/
//        Map<String, String> diseaseClassMap = loadDiseaseClass();
//        Map<Boolean, List<DiseaseItem>> hasMoreThanOneDiseaseClass = diseaseItems.stream()
//                .collect(partitioningBy((DiseaseItem di) -> di.getDiseaseClass().contains(";")));
//        List<DiseaseItem> uniqueDiseaseClass = hasMoreThanOneDiseaseClass.get(false);
//
//        // for those just have singe disease class one, replace with it's explicit name
//        uniqueDiseaseClass.stream()
//                .filter(di -> !di.getDiseaseClass().isEmpty())
//                .forEach(di -> di.setDiseaseClass(diseaseClassMap.get(di.getDiseaseClass())));
//
//        List<DiseaseItem> rs = hasMoreThanOneDiseaseClass.get(true).stream()
//                .flatMap(di -> di.cleavage(di).stream())
//                .collect(toList());
//        uniqueDiseaseClass.addAll(rs);
//        uniqueDiseaseClass.forEach(
//                diseaseItem -> diseaseItem.getGeneItems().forEach(
//                        geneItem -> geneItem.setDiseaseItem(diseaseItem)));
//        return uniqueDiseaseClass;
//    }

    private List<DiseaseItem> cleavage(List<DiseaseItem> diseaseItems) {

        List<DiseaseItem> hasMoreThanOneDiseaseClass = diseaseItems.stream().filter(diseaseItem -> diseaseItem.getDiseaseClass().contains(";")).collect(toList());
        diseaseItems.removeAll(hasMoreThanOneDiseaseClass);
        diseaseItems.addAll(hasMoreThanOneDiseaseClass.stream().flatMap(diseaseItem -> new DiseaseItem().cleavage(diseaseItem).stream()).collect(toList()));

        Map<String, String> diseaseClassMap = loadDiseaseClass();
//        diseaseItems.forEach(diseaseItem -> diseaseItem.setDiseaseClass(diseaseClassMap.get(diseaseItem.getDiseaseClass())));
//        diseaseItems.forEach(
//                diseaseItem -> diseaseItem.getGeneItems().forEach(
//                        geneItem -> geneItem.setDiseaseItem(diseaseItem)));
        return diseaseItems;
    }

    private Map<String, String> loadGeneId2AccNumMap(InputStream inputStream) throws IOException {
        /* load gene id ot UniProtKB mapping paris */
        Map<String, String> geneIdMap = new HashMap<>();
        BufferedReader TSVFile = new BufferedReader(new InputStreamReader(inputStream));
        String delimiter = "\t";
        TSVFile.readLine(); // Read header line.
        String line = TSVFile.readLine(); // Read first data row.
        while (line != null) {
            String[] split = line.split(delimiter);
            assert 2 == split.length;
            geneIdMap.put(split[1], split[0]);
            line = TSVFile.readLine();
        }
        TSVFile.close();
        return geneIdMap;
    }

    private Map<String, String> loadDiseaseClass() {
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
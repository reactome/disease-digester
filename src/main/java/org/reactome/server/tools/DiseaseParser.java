package org.reactome.server.tools;

import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    private final String GENEID_4_UNIPROT = Objects.requireNonNull(this.getClass().getClassLoader().getResource("geneid_4_uniprot.tsv")).getPath();
    private final String DISEASE_CLASS = Objects.requireNonNull(this.getClass().getClassLoader().getResource("disease-class.properties")).getPath();

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
//      <-- read table header start -->

        List<Map<String, String>> table = new ArrayList<>();


//      <-- read content start -->
        String line = TSVFile.readLine(); // Read first data row.
        while (line != null) {
            List<String> rowData = Arrays.asList(line.split(delimiter));
            Map<String, String> newRow = new LinkedHashMap<>();
//            Map<String, String> newRow = new HashMap<>();
            assert headers.size() == rowData.size();
            for (int i = 0; i < headers.size(); i++) {
                newRow.put(headers.get(i), rowData.get(i).trim());
            }
            table.add(newRow);
            line = TSVFile.readLine(); // Read next line of data.
        }
        TSVFile.close();
        return cleavageDiseaseClass(transferTable2DiseaseItems(table));
    }

    private List<DiseaseItem> transferTable2DiseaseItems(List<Map<String, String>> table) throws IOException {
        List<String> diseaseFields = Arrays.stream(DiseaseItem.class.getDeclaredFields()).map(Field::getName).filter(s -> s.contains("disease")).collect(toList());
        List<String> geneFields = Arrays.stream(GeneItem.class.getDeclaredFields()).map(Field::getName).filter(s -> s.contains("gene")).collect(toList());

        List<DiseaseItem> diseaseItems = new ArrayList<>();
        Map<Map<String, String>, List<Map<String, String>>> byDiseaseName = table.stream()
                .collect(Collectors.groupingBy((Map<String, String> m) -> diseaseFields.stream().collect(toMap(Function.identity(), m::get)),
                        mapping((Map<String, String> n) -> geneFields.stream().collect(toMap(Function.identity(), n::get)), toList())));

        Map<String, String> geneId2AccNumMap = loadGeneId2AccNumMap(GENEID_4_UNIPROT);
        byDiseaseName.forEach((key, value) -> {
            DiseaseItem diseaseItem = new DiseaseItem(key.get("diseaseId"), key.get("diseaseName").replaceAll("[^\\w]+", "_"), key.get("diseaseClass"));
            List<GeneItem> geneItems = value.stream().map(g -> new GeneItem(
                    g.get("geneId"),
                    g.get("geneSymbol"),
//                        geneId2AccNumMap.get(g.get("geneId")))).sorted(Comparator.comparing(GeneItem::getGeneSymbol)).collect(toList()))));
                    geneId2AccNumMap.get(g.get("geneId")), diseaseItem)).collect(toList());
            diseaseItem.setGeneItems(geneItems);
            diseaseItems.add(diseaseItem);
        });
        return diseaseItems;
    }

    private DiseaseItem cleavage(DiseaseItem diseaseItem, String diseaseClass) {
        DiseaseItem neonatal = (DiseaseItem) diseaseItem.clone();
        neonatal.setDiseaseClass(diseaseClass);
        return neonatal;
    }

    private List<DiseaseItem> cleavageDiseaseClass(List<DiseaseItem> diseaseItems) throws IOException {
        Map<String, String> diseaseClassMap = loadDiseaseClass();
        Map<Boolean, List<DiseaseItem>> hasMoreThanOneDiseaseClass = diseaseItems.stream().collect(partitioningBy((DiseaseItem di) -> di.getDiseaseClass().contains(";")));
        List<DiseaseItem> uniqueDiseaseClass = hasMoreThanOneDiseaseClass.get(false);
        uniqueDiseaseClass.stream().filter(di -> !di.getDiseaseClass().isEmpty())
                .forEach(di -> di.setDiseaseClass(diseaseClassMap.get(di.getDiseaseClass())));
        uniqueDiseaseClass.addAll(hasMoreThanOneDiseaseClass.get(true).stream().map(
                di -> Arrays.stream(di.getDiseaseClass().split(";")).map(cl -> cleavage(di, diseaseClassMap.get(cl))).collect(toList())).flatMap(Collection::stream).collect(toList()));
        return uniqueDiseaseClass;
    }

    private Map<String, String> loadGeneId2AccNumMap(String fileName) throws IOException {
        Map<String, String> geneIdMap = new HashMap<>();
        BufferedReader TSVFile = new BufferedReader(new FileReader(fileName));
        String delimiter = fileName.endsWith(".tsv") ? "\t" : ",";
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

    private Map<String, String> loadDiseaseClass() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DISEASE_CLASS));
        return properties.entrySet().stream().collect(toMap(e -> e.getKey().toString().trim(), e -> e.getValue().toString().trim()));
    }
}
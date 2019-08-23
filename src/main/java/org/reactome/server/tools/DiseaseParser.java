package org.reactome.server.tools;

import com.martiansoftware.jsap.JSAPResult;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DiseaseParser {

    private List<DiseaseItem> diseaseItems;


    DiseaseParser(JSAPResult config) throws Exception {
        setDiseaseItems(getDiseaseItemsFromFile(config));
    }

    List<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    private void setDiseaseItems(List<DiseaseItem> diseaseItems) {
        this.diseaseItems = diseaseItems;
    }

    private List<DiseaseItem> getDiseaseItemsFromFile(JSAPResult config) throws Exception {
        return getDiseaseItemsFromFile(config.getString("fileName"));
    }

    private BufferedReader getTSVFile(String fileName) throws IOException {
        BufferedReader TSVFile = new BufferedReader(new FileReader(fileName));
        String delimiter = fileName.endsWith(".tsv") ? "\t" : ",";
        TSVFile.readLine();
        return TSVFile;
    }

    private Map<String, String> getGeneIdMap(String fileName) throws IOException {
        Map<String, String> geneMap = new HashMap<>();
        BufferedReader TSVFile = new BufferedReader(new FileReader(fileName));
        String delimiter = fileName.endsWith(".tsv") ? "\t" : ",";
        TSVFile.readLine();
        String line = TSVFile.readLine();
        StringTokenizer st;
        while (line != null) {
            st = new StringTokenizer(line, delimiter);
            geneMap.put(st.nextElement().toString(), st.nextElement().toString());
            line = TSVFile.readLine();
        }
        TSVFile.close();
        return geneMap;
    }
//    todo: use lambada
    private List<DiseaseItem> getDiseaseItemsFromFile(String fileName) throws Exception {

        BufferedReader TSVFile = new BufferedReader(new FileReader(fileName));
        String delimiter = fileName.endsWith(".tsv") ? "\t" : ",";

//      <-- read table header start -->
        String headerLine = TSVFile.readLine(); // Read header line.
        List<String> columns = Arrays.asList(Objects.requireNonNull(headerLine.split(delimiter)));
        int[] columnIndex = new int[]{
                columns.indexOf("diseaseId"),
                columns.indexOf("diseaseName"),
                columns.indexOf("geneId"),
                columns.indexOf("geneSymbol"),
        };
//      <-- read table header start -->

        List<List<String>> dataRaw = new ArrayList<>();

//      <-- read content start -->
        String line = TSVFile.readLine(); // Read first data row.
        StringTokenizer st;
        while (line != null) {
            st = new StringTokenizer(line, delimiter);
            List<String> dataArray = new ArrayList<>();
            while (st.hasMoreElements()) {
                dataArray.add(st.nextElement().toString());
            }
            dataRaw.add(Arrays.asList(
                    dataArray.get(columnIndex[0]),
                    dataArray.get(columnIndex[1]),
                    dataArray.get(columnIndex[2])));
            line = TSVFile.readLine(); // Read next line of data.
        }
        TSVFile.close();
//      <-- read content stop -->

//      <-- digest data start: group items by diseaseName, and aggregate genes -->
        Map<String, String> disease = new HashMap<>();
        Map<String, Set<String>> overlay = new HashMap<>();
        String diseaseId;
        String diseaseName;
        String geneSymbol;
        for (List<String> row : dataRaw) {
            diseaseId = row.get(0);
            diseaseName = row.get(1);
            geneSymbol = row.get(2);
            if (!overlay.containsKey(diseaseId)) {
                disease.put(diseaseId, diseaseName.replaceAll("[^\\w]+", "_"));
                overlay.put(diseaseId, new HashSet<>(Collections.singletonList(geneSymbol)));
            }
            overlay.get(diseaseId).add(geneSymbol);
        }
//      <-- digest data stop -->

        List<DiseaseItem> diseaseItems = new ArrayList<>();
        for (Map.Entry<String, String> entry : disease.entrySet()) {
            /*
              entry.key is disease id
              entry.value is disease name
              overlay.key is disease id
              overlay.value is gene set
             */
            DiseaseItem diseaseItem = new DiseaseItem();
            diseaseItem.setDiseaseId(entry.getKey());
            diseaseItem.setDiseaseName(entry.getValue());
            Set<String> geneSet = overlay.get(entry.getKey());
//            diseaseItem.setGeneList((new ArrayList<GeneItem>(geneSet)));
            diseaseItems.add(diseaseItem);
        }
        return diseaseItems;
    }
}
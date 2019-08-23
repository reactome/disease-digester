package org.reactome.server.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class test {
    public static void main(String[] args) throws IOException {
        String fileName  ="C:\\Users\\Byron\\IDEAProjects\\disease-digester\\src\\test\\resources\\curated_gene_disease_associations.tsv";
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
        System.out.println(columns);
//        Map<String,List<DiseaseItem>> userMap = allUserList.parallelStream().collect(Collectors.groupingBy(DiseaseItem::getDiseaseId));
    }
}
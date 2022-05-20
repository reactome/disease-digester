package org.reactome.server.tools;

import org.reactome.server.domain.DataRow;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.GDA;
import org.reactome.server.domain.model.Gene;
import org.reactome.server.domain.model.SourceDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class DisGeNetParser implements Parser {
    /*
    This disease parser is hard-coded correspond to the tsv file
     */

    private final Map<String, Disease> idToDisease = new HashMap<>();
    private final Map<String, Gene> idToGene = new HashMap<>();
    private List<GDA> gdaList;
    private static final String DELIMITER = "\t";
    private final InputStream GENEID_4_UNIPROT = this.getClass().getClassLoader().getResourceAsStream("geneid_to_uniprot.tsv");
//    private final InputStream DISEASE_CLASS = this.getClass().getClassLoader().getResourceAsStream("disease-class.properties");

    DisGeNetParser(BufferedReader file) throws Exception {
        setGdaList(loadDiseases(file));
    }

    public List<GDA> getGdaList() {
        return gdaList;
    }

    public Collection<Gene> getGenes() {
        return idToGene.values();
    }

    public Collection<Disease> getDiseases() {
        return idToDisease.values();
    }

    private void setGdaList(List<GDA> gdaList) {
        this.gdaList = gdaList;
    }

    private List<GDA> loadDiseases(BufferedReader file) {
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
        return parseRows(dataRowList);
    }

    private List<GDA> parseRows(List<DataRow> dataRowList) {
        Map<String, String> geneId2AccNumMap = loadGeneId2AccNumMap();
        return dataRowList.stream().map(dataRow -> new GDA(
                        idToDisease.computeIfAbsent(dataRow.getDiseaseId(), s -> new Disease(dataRow.getDiseaseId(), dataRow.getDiseaseName(), SourceDatabase.DISGENET)),
                        idToGene.computeIfAbsent(dataRow.getGeneId(), s1 -> new Gene(dataRow.getGeneId(), dataRow.getGeneSymbol(), geneId2AccNumMap.get(dataRow.getGeneId()))),
                        dataRow.getScore()
                )
        ).collect(Collectors.toList());
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
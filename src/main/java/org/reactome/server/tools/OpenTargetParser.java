package org.reactome.server.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactome.server.domain.OpenTargetRow;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.GDA;
import org.reactome.server.domain.model.Gene;
import org.reactome.server.domain.model.SourceDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class OpenTargetParser implements Parser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Gene> idToGene = new HashMap<>();
    private final Map<GDA, List<Float>> gdaToScores = new HashMap<>();
    private final List<GDA> gdaList = new ArrayList<>();
    private final List<Disease> diseases = new ArrayList<>();


    public OpenTargetParser(BufferedReader file) {
        file.lines().forEach(s -> {
            try {
                OpenTargetRow row = mapper.readValue(s, OpenTargetRow.class);
                Disease disease = new Disease(row.getDiseaseId(), row.getDiseaseLabel(), SourceDatabase.OPEN_TARGET);
                diseases.add(disease);
                row.getDetails().forEach(detail -> {
                    Gene gene = idToGene.computeIfAbsent(detail.getTarget(), ensemblId -> new Gene(ensemblId, ensemblId, ensemblId));
                    gdaToScores.computeIfAbsent(new GDA(disease, gene, 0, detail.getVariantId()), gda -> new ArrayList<>()).add(detail.getL2g());
                });

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        gdaToScores.forEach((gda, floats) -> {
            gda.setScore((float) floats.stream().mapToDouble(Float::doubleValue).average().orElse(0));
            gdaList.add(gda);
        });


        AtomicInteger amount = new AtomicInteger();

        Utils.slice(idToGene.keySet(), 300).forEach(refBatch -> {
            System.out.println("UNIPROT enrichment: " + amount + " / " + idToGene.size());
            amount.addAndGet(refBatch.size());
            Map<String, Object> postData = Map.of(
                    "query", String.join(" ", refBatch),
                    "from", "ENSEMBL_ID",
                    "to", "SWISSPROT",
                    "columns", "id,genes(PREFERRED)",
                    "format", "tab"
            );

            Arrays.stream(Utils.queryUniprot(postData).split("\n"))
                    .skip(1) // Skip header
                    .forEach(line -> {
                        String[] uniprotRow = line.split("\t");
                        for (String ensembl : uniprotRow[2].split(",")) {
                            Gene gene = idToGene.get(ensembl);
                            if (gene != null) {
                                gene.setGeneSymbol(uniprotRow[1].split(";")[0]);
                                gene.setAccessionNumber(uniprotRow[0]);
                            }
                        }
                    });

        });


    }

    public Map<String, Gene> getIdToGene() {
        return idToGene;
    }

    public Collection<Gene> getGenes() {
        return idToGene.values();
    }

    public List<GDA> getGdaList() {
        return gdaList;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }
}

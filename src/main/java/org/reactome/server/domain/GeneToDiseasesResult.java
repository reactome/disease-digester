package org.reactome.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.reactome.server.repository.out.GeneToDiseases;

import java.util.List;

@Data
@AllArgsConstructor
public class GeneToDiseasesResult {
    private String resource;
    private List<GeneToDiseases> entities;
}

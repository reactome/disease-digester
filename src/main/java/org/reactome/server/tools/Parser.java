package org.reactome.server.tools;

import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.GDA;
import org.reactome.server.domain.model.Gene;

import java.util.Collection;

public interface Parser {
    Collection<GDA> getGdaList();

    Collection<Disease> getDiseases();

    Collection<Gene> getGenes();
}

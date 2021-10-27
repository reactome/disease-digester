package org.reactome.server.domain.analysis;

public enum SortBy {
    NAME("NAME"),
    TOTAL_ENTITIES("TOTAL_ENTITIES"),
    TOTAL_INTERACTORS("TOTAL_INTERACTORS"),
    TOTAL_REACTIONS("TOTAL_REACTIONS"),
    FOUND_ENTITIES("FOUND_ENTITIES"),
    FOUND_INTERACTORS("FOUND_INTERACTORS"),
    FOUND_REACTIONS("FOUND_REACTIONS"),
    ENTITIES_RATIO("ENTITIES_RATIO"),
    ENTITIES_PVALUE("ENTITIES_PVALUE"),
    ENTITIES_FDR("ENTITIES_FDR"),
    REACTIONS_RATIO("REACTIONS_RATIO");
    private final String sort;

    SortBy(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }
}

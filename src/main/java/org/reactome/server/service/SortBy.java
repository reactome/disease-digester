package org.reactome.server.service;

public enum SortBy {
    GENE("gene"), NAME("name");

    private final String sort;

    SortBy(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }
}

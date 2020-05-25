package org.reactome.server.service;

public enum OrderBy {
    ASC("ASC"), DESC("DESC");

    private String order;

    OrderBy(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }
}

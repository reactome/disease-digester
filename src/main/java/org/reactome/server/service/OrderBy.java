package org.reactome.server.service;

public enum OrderBy {
    ASC("asc"), DESC("desc");

    private String order;

    OrderBy(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }
}

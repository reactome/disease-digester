package org.reactome.server.domain.analysis;

public enum Resource {
    TOTAL("TOTAL"),
    UNIPROT("TOTAL"),
    ENSEMBL("ENSEMBL"),
    CHEBI("CHEBI"),
    IUPHAR("IUPHAR"),
    MIRBASE("MIRBASE"),
    NCBI_PROTEIN("NCBI_PROTEIN"),
    EMBL("EMBL"),
    COMPOUND("COMPOUND"),
    PUBCHEM_COMPOUND("PUBCHEM_COMPOUND");

    private final String resource;

    Resource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}

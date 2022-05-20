package org.reactome.server.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GDAKey implements Serializable {

    @Column(name = "diseaseId")
    private String diseaseId;

    @Column(name = "geneId")
    private String geneId;

    private String variantId;
    public GDAKey() {
    }

    public GDAKey(String diseaseId, String geneId) {
        this.diseaseId = diseaseId;
        this.geneId = geneId;
        this.variantId = "";
    }

    public GDAKey(String diseaseId, String geneId, String variantId) {
        this.diseaseId = diseaseId;
        this.geneId = geneId;
        this.variantId = variantId;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GDAKey gdaKey = (GDAKey) o;
        return getDiseaseId().equals(gdaKey.getDiseaseId()) && getGeneId().equals(gdaKey.getGeneId()) && Objects.equals(variantId, gdaKey.variantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDiseaseId(), getGeneId(), variantId);
    }
}

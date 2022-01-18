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

    public GDAKey() {
    }

    public GDAKey(String diseaseId, String geneId) {
        this.diseaseId = diseaseId;
        this.geneId = geneId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GDAKey gdaKey = (GDAKey) o;
        return diseaseId.equals(gdaKey.diseaseId) && geneId.equals(gdaKey.geneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diseaseId, geneId);
    }
}

package org.reactome.server.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "gda")

public class GDA implements Serializable {
    @EmbeddedId
    private GDAKey id;

    @ManyToOne
    @MapsId("diseaseId")
    @JoinColumn(name = "diseaseId")
    private Disease disease;

    @ManyToOne
    @MapsId("geneId")
    @JoinColumn(name = "geneId")
    private Gene gene;

    private float score;

    public GDA() {
    }

    public GDA(Disease disease, Gene gene, float score) {
        this.id = new GDAKey(disease.getDiseaseId(), gene.getGeneId());
        this.disease = disease;
        this.gene = gene;
        this.score = score;
    }

    public GDA(Disease disease, Gene gene, float score, String variantId) {
        this.id = new GDAKey(disease.getDiseaseId(), gene.getGeneId(), variantId);
        this.disease = disease;
        this.gene = gene;
        this.score = score;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
        this.id.setDiseaseId(disease.getDiseaseId());
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
        this.id.setGeneId(gene.getGeneId());
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getVariantId() {
        return id.getVariantId();
    }

    public void setVariantId(String variantId) {
        this.id.setVariantId(variantId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GDA gda = (GDA) o;
        return Objects.equals(id, gda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GDA{" +
                "disease=" + disease +
                ", gene=" + gene +
                ", score=" + score +
                ", variantId='" + getVariantId() + '\'' +
                '}';
    }
}

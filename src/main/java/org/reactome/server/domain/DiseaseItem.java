package org.reactome.server.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.reactome.server.util.DiseaseItemSerializer;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Cacheable
@Table(name = "disease")
@JsonSerialize(using = DiseaseItemSerializer.class)
public class DiseaseItem {
    @Id
    private String diseaseId;
    private String diseaseName;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "diseaseId")
    private List<GeneItem> geneItems;

    public DiseaseItem() {
    }

    public DiseaseItem(String diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }

    public DiseaseItem(String diseaseId, String diseaseName, List<GeneItem> geneItems) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.geneItems = geneItems;
    }

    /*Rewrite the equals & hashcode method so that the collectors.groupBy can work correctly */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiseaseItem that = (DiseaseItem) o;
        return Objects.equals(diseaseId, that.diseaseId) &&
                Objects.equals(diseaseName, that.diseaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diseaseId, diseaseName);
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public List<GeneItem> getGeneItems() {
        return geneItems;
    }

    public void setGeneItems(List<GeneItem> geneItems) {
        this.geneItems = geneItems;
    }

    @Override
    public String toString() {
        return "DiseaseItem{" +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", geneItems=" + geneItems +
                '}';
    }
}

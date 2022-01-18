package org.reactome.server.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.reactome.server.util.DiseaseSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Cacheable
@Table(name = "disease")
@Entity
@JsonSerialize(using = DiseaseSerializer.class)
public class Disease implements Serializable {
    @Id
    private String diseaseId;
    private String diseaseName;
    @OneToMany(mappedBy = "disease")
    private List<GDA> associatedGenes;

    public Disease() {
    }

    public Disease(String diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }

    public Disease(String diseaseId, String diseaseName, List<GDA> associatedGenes) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.associatedGenes = associatedGenes;
    }

    /*Rewrite the equals & hashcode method so that the collectors.groupBy can work correctly */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease that = (Disease) o;
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

    public List<GDA> getAssociatedGenes() {
        return associatedGenes;
    }

    public void setAssociatedGenes(List<GDA> associatedGenes) {
        this.associatedGenes = associatedGenes;
    }

    @Override
    public String toString() {
        return "Disease{" +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                '}';
    }
}

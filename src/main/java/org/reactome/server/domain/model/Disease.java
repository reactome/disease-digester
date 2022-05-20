package org.reactome.server.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.reactome.server.util.DiseaseSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Enumerated(EnumType.ORDINAL)
    private SourceDatabase source;

    public Disease() {
    }

    public Disease(String diseaseId, String diseaseName, SourceDatabase source) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.source = source;
    }

    public Disease(String diseaseId, String diseaseName, SourceDatabase source, List<GDA> associatedGenes) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.source = source;
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

    public SourceDatabase getSource() {
        return source;
    }

    public void setSource(SourceDatabase source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Disease{" +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", source='" + source.name() + '\'' +
                '}';
    }
}

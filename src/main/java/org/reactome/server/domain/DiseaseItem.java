package org.reactome.server.domain;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.reactome.server.util.DiseaseItemSerializer;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Entity
//@Table(name = "disease", indexes = {@Index(name = "disease_id_index", columnList = "id")})
@Table(name = "disease")
@JsonSerialize(using = DiseaseItemSerializer.class)
public class DiseaseItem implements Comparable {
    @Id
//    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diseaseId;
    private String diseaseName;
    private String diseaseClass;
    @ManyToMany(targetEntity = GeneItem.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GeneItem> geneItems;

    public DiseaseItem() {
    }

    public DiseaseItem(String diseaseId, String diseaseName, String diseaseClass) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.diseaseClass = diseaseClass;
    }

    public DiseaseItem(String diseaseId, String diseaseName, String diseaseClass, List<GeneItem> geneItems) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.diseaseClass = diseaseClass;
        this.geneItems = geneItems;
    }

    private DiseaseItem(DiseaseItem diseaseItem) {
        this.diseaseId = diseaseItem.diseaseId;
        this.diseaseName = diseaseItem.diseaseName;
    }

    public List<DiseaseItem> cleavage(DiseaseItem diseaseItem) {
        return Arrays.stream(diseaseItem.getDiseaseClass().split(";"))
                .map(dc -> {
                    DiseaseItem cloned = new DiseaseItem(diseaseItem);
                    cloned.setDiseaseClass(dc);
                    return cloned;
                }).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDiseaseClass() {
        return diseaseClass;
    }

    public void setDiseaseClass(String diseaseClass) {
        this.diseaseClass = diseaseClass;
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
                "id=" + id +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", diseaseClass='" + diseaseClass + '\'' +
                ", geneItems=" + geneItems +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        DiseaseItem that = (DiseaseItem) o;
        return diseaseName.compareTo(that.getDiseaseName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiseaseItem that = (DiseaseItem) o;
        return Objects.equals(diseaseName, that.diseaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diseaseName);
    }
}

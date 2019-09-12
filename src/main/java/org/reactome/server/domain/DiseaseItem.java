package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Entity
//@Table(name = "disease", indexes = {@Index(name = "disease_id_index", columnList = "id")})
@Table(name = "disease")
public class DiseaseItem {
    @Id
    @JsonIgnore
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
}

package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "disease")
public class DiseaseItem {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diseaseId;
    private String diseaseName;
    private String diseaseClass;
    @ManyToMany(targetEntity = GeneItem.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // TODO: 19-8-30 use join table setting to reduce the query time
    private Set<GeneItem> geneItems;

    public DiseaseItem() {
    }

    public DiseaseItem(String diseaseId, String diseaseName, String diseaseClass) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.diseaseClass = diseaseClass;
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

    public Set<GeneItem> getGeneItems() {
        return geneItems;
    }

    public void setGeneItems(Set<GeneItem> geneItems) {
        this.geneItems = geneItems;
    }

    @Override
    public String toString() {
        return "DiseaseItem{" +
                "id='" + id + '\'' +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", diseaseClass='" + diseaseClass + '\'' +
                ", geneItems=" + geneItems +
                '}';
    }
}

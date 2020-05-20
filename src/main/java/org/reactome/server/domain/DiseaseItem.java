package org.reactome.server.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.reactome.server.util.DiseaseItemSerializer;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "disease")
@JsonSerialize(using = DiseaseItemSerializer.class)
public class DiseaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diseaseId;
    private String diseaseName;
    @OneToMany(targetEntity = GeneItem.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GeneItem> geneItems;

    public DiseaseItem() {
    }

    public DiseaseItem(String diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
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
                ", geneItems=" + geneItems +
                '}';
    }
}

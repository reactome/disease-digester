package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "gene")
public class GeneItem {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToMany(targetEntity = DiseaseItem.class, mappedBy = "geneItems")
    private Set<DiseaseItem> diseaseItems;
    @JsonIgnore
    private String geneId;
    private String geneSymbol;
    @JsonIgnore
    private String accessionNumber;

    public GeneItem() {
    }

    public GeneItem(String geneId, String geneSymbol) {
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    public void setDiseaseItems(Set<DiseaseItem> diseaseItems) {
        this.diseaseItems = diseaseItems;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    @Override
    public String toString() {
        return "GeneItem{" +
                "id='" + id + '\'' +
                ", geneId='" + geneId + '\'' +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", accessionNumber='" + accessionNumber + '\'' +
                '}';
    }
}
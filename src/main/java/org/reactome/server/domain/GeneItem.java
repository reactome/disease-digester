package org.reactome.server.domain;

import javax.persistence.*;

@Entity
@Table(name = "gene")
public class GeneItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @ManyToMany(targetEntity = DiseaseItem.class, mappedBy = "geneItems")
//    private Set<DiseaseItem> diseaseItems;
    private String diseaseId;
    private String geneId;
    private String geneSymbol;
    private String accessionNumber;
    private Float score;

    public GeneItem() {
    }

    public GeneItem(String geneId, String diseaseId, String geneSymbol, Float score) {
        this.geneId = geneId;
        this.diseaseId = diseaseId;
        this.geneSymbol = geneSymbol;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Set<DiseaseItem> getDiseaseItems() {
//        return diseaseItems;
//    }

//    public void setDiseaseItems(Set<DiseaseItem> diseaseItems) {
//        this.diseaseItems = diseaseItems;
//    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
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

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "GeneItem{" +
                "id=" + id +
                ", diseaseId='" + diseaseId + '\'' +
                ", geneId='" + geneId + '\'' +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", accessionNumber='" + accessionNumber + '\'' +
                ", score=" + score +
                '}';
    }
}
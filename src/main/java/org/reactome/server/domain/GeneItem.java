package org.reactome.server.domain;

import javax.persistence.*;

@Entity
@Cacheable
@Table(name = "gene")
public class GeneItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String geneId;
    private String geneSymbol;
    private String accessionNumber;
    private float score;

    public GeneItem() {
    }

    public GeneItem(String geneId, String geneSymbol, String accessionNumber, float score) {
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
        this.accessionNumber = accessionNumber;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "GeneItem{" +
                "id=" + id +
                ", geneId='" + geneId + '\'' +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", accessionNumber='" + accessionNumber + '\'' +
                ", score=" + score +
                '}';
    }
}
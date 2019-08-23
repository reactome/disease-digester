package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "disease_gene")
public class GeneItem {
    @Id
    @Column(name = "id", nullable = false, length = 16, unique = true)
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @JsonIgnore
    @ManyToOne
    private DiseaseItem diseaseItem;
    private String geneId;
    private String geneSymbol;
    private String geneACCNum;

    public GeneItem() {
    }

    public GeneItem(DiseaseItem diseaseItem, String geneId, String geneSymbol, String geneACCNum) {
        this.diseaseItem = diseaseItem;
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
        this.geneACCNum = geneACCNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DiseaseItem getDiseaseItem() {
        return diseaseItem;
    }

    public void setDiseaseItem(DiseaseItem diseaseItem) {
        this.diseaseItem = diseaseItem;
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

    public String getGeneACCNum() {
        return geneACCNum;
    }

    public void setGeneACCNum(String geneACCNum) {
        this.geneACCNum = geneACCNum;
    }

    @Override
    public String toString() {
        return "GeneItem{" +
                "id='" + id + '\'' +
                ", geneId='" + geneId + '\'' +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", geneACCNum='" + geneACCNum + '\'' +
                '}';
    }
}

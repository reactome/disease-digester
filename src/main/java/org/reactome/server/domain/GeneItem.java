package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "disease_gene")
public class GeneItem implements Cloneable {
    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false, length = 32, unique = true)
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DiseaseItem diseaseItem;
    private String geneId;
    private String geneSymbol;
    private String accessionNumber;

    public GeneItem() {
    }

    public GeneItem(String geneId, String geneSymbol, String accessionNumber, DiseaseItem diseaseItem) {
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
        this.accessionNumber = accessionNumber;
        this.diseaseItem = diseaseItem;
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

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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

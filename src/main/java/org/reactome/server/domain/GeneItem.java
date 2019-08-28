package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "gene")
public class GeneItem {
    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false, length = 32, unique = true)
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToMany(targetEntity = DiseaseItem.class, fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @ManyToOne(fetch = FetchType.EAGER)
    private List<DiseaseItem> diseaseItems;
    private String geneId;
    private String geneSymbol;
    private String accessionNumber;

    public GeneItem() {
    }

    public GeneItem(String geneId, String geneSymbol, String accessionNumber) {
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
        this.accessionNumber = accessionNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DiseaseItem> getDiseaseItems() {
        return diseaseItems;
    }

    public void setDiseaseItems(List<DiseaseItem> diseaseItems) {
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

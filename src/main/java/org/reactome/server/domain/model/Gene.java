package org.reactome.server.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Cacheable
@Entity
@Table(name = "gene")
public class Gene implements Serializable {
    @Id
    private String geneId;
    private String geneSymbol;
    private String accessionNumber;
    @OneToMany(mappedBy = "gene")
    private List<GDA> associatedDiseases;

    public Gene() {
    }

    public Gene(String geneId, String geneSymbol, String accessionNumber) {
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
        this.accessionNumber = accessionNumber;
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

    public List<GDA> getAssociatedDiseases() {
        return associatedDiseases;
    }

    public void setAssociatedDiseases(List<GDA> associatedDiseases) {
        this.associatedDiseases = associatedDiseases;
    }

    @Override
    public String toString() {
        return "GeneItem{" +
                "geneId='" + geneId + '\'' +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", accessionNumber='" + accessionNumber + '\'' +
                '}';
    }
}
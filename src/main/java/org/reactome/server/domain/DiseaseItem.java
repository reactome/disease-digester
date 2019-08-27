package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "disease")
public class DiseaseItem implements Cloneable {
    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false, length = 32, unique = true)
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    private String diseaseId;
    private String diseaseName;
    private String diseaseClass;
    //    The value of mappedBy attributes is the name of the class field on the other side of the relationship
    @OneToMany(mappedBy = "diseaseItem", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GeneItem> geneItems;

    public DiseaseItem() {
    }

    public DiseaseItem(String diseaseId, String diseaseName, String diseaseClass) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.diseaseClass = diseaseClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public Object clone() {
        DiseaseItem clonedDisease = null;
        try {
            clonedDisease = (DiseaseItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        List<GeneItem> clonedGene = new ArrayList<>();
        Objects.requireNonNull(clonedDisease).getGeneItems().forEach(oldGene -> {
            try {
                clonedGene.add((GeneItem) oldGene.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
        clonedDisease.setGeneItems(clonedGene);
        return clonedDisease;
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

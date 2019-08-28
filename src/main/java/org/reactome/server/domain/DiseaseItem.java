package org.reactome.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Entity
@Table(name = "disease")
public class DiseaseItem {
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
//    @OneToMany(mappedBy = "diseaseItem", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToMany(targetEntity = GeneItem.class,mappedBy = "diseaseItems",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<GeneItem> geneItems;

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
//        return Arrays.stream(diseaseItem.getDiseaseClass().split(";"))
//        System.out.println(diseaseItem.getDiseaseClass()+"for :"+Arrays.stream(diseaseItem.getDiseaseClass().split(";")).collect(Collectors.toList()));
        List<DiseaseItem> clonedDiseaseItems = Arrays.stream(diseaseItem.getDiseaseClass().split(";"))
                .map(dc -> {
                    DiseaseItem cloned = new DiseaseItem(diseaseItem);
                    cloned.setDiseaseClass(dc);
                    return cloned;
                }).collect(Collectors.toList());

        clonedDiseaseItems.forEach(d -> d.getGeneItems().forEach(geneItem -> geneItem.setDiseaseItems(clonedDiseaseItems)));
        return clonedDiseaseItems;
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

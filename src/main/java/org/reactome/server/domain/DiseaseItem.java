package org.reactome.server.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "disease")
public class DiseaseItem {
    @Id
    @Column(name = "id", nullable = false, length = 32, unique = true)
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
    @GeneratedValue(generator = "idGenerator")
    private String id;
    private String diseaseId;
    private String diseaseName;
    @ElementCollection
//    @OneToMany(fetch = FetchType.EAGER)
    private List<String> geneList;

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

    public List<String> getGeneList() {
        return geneList;
    }

    public void setGeneList(List<String> geneList) {
        this.geneList = geneList;
    }

    @Override
    public String toString() {
        return "DiseaseItem{" +
                "id='" + id + '\'' +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", geneList=" + geneList +
                '}';
    }
}

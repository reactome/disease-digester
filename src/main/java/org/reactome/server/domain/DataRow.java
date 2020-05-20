package org.reactome.server.domain;

import java.util.Objects;

public class DataRow {
    private String geneId;
    private String geneSymbol;
    private String diseaseId;
    private String diseaseName;
    private Float score;

    public DataRow(String geneId, String geneSymbol, String diseaseId, String diseaseName, Float score) {
        this.geneId = geneId;
        this.geneSymbol = geneSymbol;
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
        this.score = score;
    }

    public String getDiseaseId() {
        return diseaseId;
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

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataRow dataRow = (DataRow) o;
        return Objects.equals(diseaseId, dataRow.diseaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diseaseId);
    }

    @Override
    public String toString() {
        return "DataRow{" +
                "geneId='" + geneId + '\'' +
                ", geneSymbol='" + geneSymbol + '\'' +
                ", diseaseId='" + diseaseId + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}

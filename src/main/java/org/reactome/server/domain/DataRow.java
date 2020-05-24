package org.reactome.server.domain;

public class DataRow {
    private String geneId;
    private String geneSymbol;
    private String diseaseId;
    private String diseaseName;
    private float score;

    public DataRow(String geneId, String geneSymbol, String diseaseId, String diseaseName, float score) {
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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

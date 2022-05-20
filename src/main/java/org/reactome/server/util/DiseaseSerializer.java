package org.reactome.server.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.GDA;
import org.reactome.server.domain.model.SourceDatabase;

import java.io.IOException;

public class DiseaseSerializer extends StdSerializer<Disease> {
    protected DiseaseSerializer(Class<Disease> t) {
        super(t);
    }

    public DiseaseSerializer() {
        super(Disease.class);
    }

    @Override
    public void serialize(Disease disease, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("diseaseId", disease.getDiseaseId());
        jsonGenerator.writeStringField("diseaseName", disease.getDiseaseName());
        jsonGenerator.writeFieldName("geneItems");
        jsonGenerator.writeStartArray();
        switch (disease.getSource()) {
            case DISGENET:
                for (GDA gda : disease.getAssociatedGenes()) {
                    jsonGenerator.writeString(gda.getGene().getGeneSymbol());
                }
                break;
            case OPEN_TARGET:
                for (String s : disease.getAssociatedGeneIds()) {
                    jsonGenerator.writeString(s);
                }
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}

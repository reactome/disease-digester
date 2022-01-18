package org.reactome.server.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.reactome.server.domain.model.Disease;
import org.reactome.server.domain.model.GDA;

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
//      "diseaseId": "C4017238",
//      "diseaseName": "Diabetes_Mellitus_Type_2_Protection_Against",
//      "geneItems": [Array]
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("diseaseId", disease.getDiseaseId());
        jsonGenerator.writeStringField("diseaseName", disease.getDiseaseName());
        jsonGenerator.writeFieldName("geneItems");
        jsonGenerator.writeStartArray();
        for (GDA gda : disease.getAssociatedGenes()) {
            jsonGenerator.writeString(gda.getGene().getGeneSymbol());
        }
        jsonGenerator.writeEndArray();
//        jsonGenerator.writeFieldName("scores");
//        jsonGenerator.writeStartArray();
//        for (GeneItem geneItem : diseaseItem.getGeneItems()) {
//            jsonGenerator.writeNumber(geneItem.getScore());
//        }
//        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}

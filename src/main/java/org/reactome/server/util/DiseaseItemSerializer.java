package org.reactome.server.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.domain.GeneItem;

import java.io.IOException;

public class DiseaseItemSerializer extends StdSerializer<DiseaseItem> {
    protected DiseaseItemSerializer(Class<DiseaseItem> t) {
        super(t);
    }

    public DiseaseItemSerializer() {
        super(DiseaseItem.class);
    }

    @Override
    public void serialize(DiseaseItem diseaseItem, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//      "diseaseId": "C4017238",
//      "diseaseName": "Diabetes_Mellitus_Type_2_Protection_Against",
//      "geneItems": [Array]
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("diseaseId", diseaseItem.getDiseaseId());
        jsonGenerator.writeStringField("diseaseName", diseaseItem.getDiseaseName());
        jsonGenerator.writeFieldName("geneItems");
        jsonGenerator.writeStartArray();
        for (GeneItem geneItem : diseaseItem.getGeneItems()) {
            jsonGenerator.writeString(geneItem.getGeneSymbol());
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

package org.reactome.server.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.reactome.server.domain.analysis.AnalysisParameter;

import java.io.IOException;

public class AnalysisSerializer extends StdSerializer<AnalysisParameter> {
    protected AnalysisSerializer(Class<AnalysisParameter> t) {
        super(t);
    }

    protected AnalysisSerializer() {
        super(AnalysisParameter.class);
    }

    @Override
    public void serialize(AnalysisParameter analysisParameter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("status", analysisParameter.getHttpStatus().value());
        if (null != analysisParameter.getUrlResult()) {
            jsonGenerator.writeStringField("url", analysisParameter.getUrlResult());
        } else {
            jsonGenerator.writeStringField("error", analysisParameter.getErrorMessage());
        }
        jsonGenerator.writeObjectFieldStart("Summary");
        jsonGenerator.writeBooleanField("projection", analysisParameter.isProjection());
        jsonGenerator.writeBooleanField("interactors", analysisParameter.isInteractors());
        jsonGenerator.writeStringField("sortBy", analysisParameter.getSortBy().getSort());
        jsonGenerator.writeStringField("order", analysisParameter.getOrder().getOrder());
        jsonGenerator.writeStringField("resource", analysisParameter.getResource().getResource());
        jsonGenerator.writeNumberField("pValue", analysisParameter.getpValue());
        jsonGenerator.writeBooleanField("includeDisease", analysisParameter.isIncludeDisease());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}
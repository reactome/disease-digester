package org.reactome.server.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.reactome.server.repository.out.GeneToDiseases;

import java.io.IOException;

public class GeneToDiseasesSerializer extends StdSerializer<GeneToDiseases> {

    public GeneToDiseasesSerializer(Class<GeneToDiseases> t) {
        super(t);
    }

    public GeneToDiseasesSerializer() {
        super(GeneToDiseases.class);
    }

    @Override
    public void serialize(GeneToDiseases value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("acc", value.getAcc());
        gen.writeNumberField("count", value.getCount());
        gen.writeFieldName("interactors");
        gen.writeRaw(":" + value.getRawJSONArrayOfDiseases());
        gen.writeEndObject();
    }
}

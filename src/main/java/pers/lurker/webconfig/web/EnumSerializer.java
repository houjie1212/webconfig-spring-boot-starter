package pers.lurker.webconfig.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import pers.lurker.webconfig.enumeration.BaseEnum;

import java.io.IOException;

public class EnumSerializer extends StdSerializer<BaseEnum> {

    public EnumSerializer(Class<BaseEnum> enumType) {
        super(enumType);
    }

    @Override
    public void serialize(BaseEnum baseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeStartObject();
//        jsonGenerator.writeStringField("code", baseEnum.getCode().toString());
//        jsonGenerator.writeEndObject();
        if (baseEnum.getCode() instanceof Integer) {
            jsonGenerator.writeNumber((int) baseEnum.getCode());
        } else {
            jsonGenerator.writeString(baseEnum.getCode().toString());
        }
    }
}

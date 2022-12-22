package pers.lurker.webconfig.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.util.StringUtils;
import pers.lurker.webconfig.enumeration.BaseEnum;

import java.io.IOException;

public class EnumSerializer extends StdSerializer<BaseEnum> {

    public EnumSerializer(Class<BaseEnum> enumType) {
        super(enumType);
    }

    @Override
    public void serialize(BaseEnum baseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.hasText(baseEnum.getValue())) {
            jsonGenerator.writeStartObject();
            if (baseEnum.getCode() instanceof Integer) {
                jsonGenerator.writeNumberField("code", (int) baseEnum.getCode());
            } else {
                jsonGenerator.writeStringField("code", baseEnum.getCode().toString());
            }
            jsonGenerator.writeStringField("value", baseEnum.getValue());
            jsonGenerator.writeEndObject();
        } else {
            if (baseEnum.getCode() instanceof Integer) {
                jsonGenerator.writeNumber((int) baseEnum.getCode());
            } else {
                jsonGenerator.writeString(baseEnum.getCode().toString());
            }
        }
    }
}

package pers.lurker.webconfig.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import pers.lurker.webconfig.support.Contants;
import pers.lurker.webconfig.util.ThreadLocalTransmittableUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

public class RequestTZDateDeSerializer extends DateDeserializers.DateDeserializer {
//    public RequestTZDateDeSerializer() { super(Date.class); }
    public RequestTZDateDeSerializer(DateDeserializers.DateDeserializer base, DateFormat df, String formatString) {
        super(base, df, formatString);
    }

    @Override
    protected RequestTZDateDeSerializer withDateFormat(DateFormat df, String formatString) {
        return new RequestTZDateDeSerializer(this, df, formatString);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return this._parseDate(p, ctxt);
    }

    @Override
    protected Date _parseDate(JsonParser p, DeserializationContext ctxt)
            throws IOException
    {
        if (_customFormat != null) {
            if (p.hasToken(JsonToken.VALUE_STRING)) {
                String str = p.getText().trim();
                if (str.length() == 0) {
                    return (Date) getEmptyValue(ctxt);
                }
                DateFormat dateFormat = (DateFormat) _customFormat.clone();
                TimeZone requestTimezone = ThreadLocalTransmittableUtil.get(Contants.REQ_TIME_ZONE, TimeZone.class);
                if(requestTimezone!=null){
                    dateFormat.setTimeZone(requestTimezone);
                }
                try {
                    return dateFormat.parse(str);
                } catch (ParseException e) {
                    return (Date) ctxt.handleWeirdStringValue(handledType(), str,
                            "expected format \"%s\"", _formatString);
                }
            }
        }
        return super._parseDate(p, ctxt);
    }
}

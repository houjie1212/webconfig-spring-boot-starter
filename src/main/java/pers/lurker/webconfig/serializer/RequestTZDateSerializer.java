package pers.lurker.webconfig.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.lurker.webconfig.support.Contants;
import pers.lurker.webconfig.util.ThreadLocalTransmittableUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RequestTZDateSerializer extends DateSerializer {

    private Logger logger = LoggerFactory.getLogger(RequestTZDateSerializer.class);

    public RequestTZDateSerializer(){
        super();
    }
    public RequestTZDateSerializer(Boolean useTimestamp, DateFormat customFormat){
        super(useTimestamp,customFormat);
    }

    @Override
    public void serialize(Date value, JsonGenerator g, SerializerProvider provider) throws IOException{
        super.serialize(value,g,provider);
    }

    @Override
    public RequestTZDateSerializer withFormat(Boolean timestamp, DateFormat customFormat) {
        return new RequestTZDateSerializer(timestamp, customFormat);
    }

    @Override
    protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException
    {
        TimeZone reqTimeZone = ThreadLocalTransmittableUtil.get(Contants.REQ_TIME_ZONE,TimeZone.class);
        if (_customFormat == null) {
            provider.defaultSerializeDateValue(value, g);
            return;
        }
//        DateFormat f = _reusedCustomFormat.getAndSet(null);
//        if (f == null) {
//            f = (DateFormat) _customFormat.clone();
//        }
//        System.out.println(reqTimeZone);
        DateFormat f = (DateFormat) _customFormat.clone();

        if(reqTimeZone!=null){
            f.setTimeZone(reqTimeZone);
        }
        logger.trace("json序列化时区:{}",f.getTimeZone());
//        System.out.println(value);
        g.writeString(f.format(value));
//        _reusedCustomFormat.compareAndSet(null, f);
    }
}

package pers.lurker.webconfig.filter.jsonparam;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JsonParameterHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public JsonParameterHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        JsonParameterRequestHolder.setJsonParameter(StreamUtils.copyToByteArray(request.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bis = new ByteArrayInputStream(JsonParameterRequestHolder.getJsonParameter());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener listener) {
            }
            @Override
            public int read() {
                return bis.read();
            }
        };
    }
}

package pers.lurker.webconfig.filter.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pers.lurker.webconfig.filter.jsonparam.JsonParameterRequestHolder;
import pers.lurker.webconfig.util.JsonUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final ObjectMapper objectMapper;
    //用来缓存从HttpServletRequest的io流中读取的参数转为字节缓存下来

    XssHttpServletRequestWrapper(HttpServletRequest servletRequest, ObjectMapper objectMapper) throws IOException {
        super(servletRequest);
        this.objectMapper = objectMapper;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXss(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXss(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXss(value);
    }

    /**
     * 过滤json格式数据
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        byte[] bytes = JsonParameterRequestHolder.getJsonParameter();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ServletInputStream sis = new ServletInputStream() {
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

        if (super.getHeader(HttpHeaders.CONTENT_TYPE) == null ||
                !super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            return sis;
        }
        Object xssResult = objectMapper.readValue(bytes, Object.class);
        if (xssResult == null) {
            return sis;
        }

        ByteArrayInputStream xssBis = new ByteArrayInputStream(JsonUtil.obj2String(xssResult).getBytes(StandardCharsets.UTF_8));
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
                return xssBis.read();
            }
        };
    }

    private String cleanXss(String value) {
        return StringEscapeUtils.escapeHtml4(value);
    }
}

package pers.lurker.webconfig.filter.jsonparam;

import org.springframework.core.NamedThreadLocal;

public class JsonParameterRequestHolder {

    private static final ThreadLocal<byte[]> jsonParameterHolder = new NamedThreadLocal<>("json parameter");

    public static byte[] getJsonParameter() {
        return jsonParameterHolder.get();
    }

    public static void setJsonParameter(byte[] bytes) {
        jsonParameterHolder.set(bytes);
    }

    public static void removeJsonParameter() {
        jsonParameterHolder.remove();
    }
}

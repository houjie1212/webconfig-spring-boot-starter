package pers.lurker.webconfig.exception.ext;


import pers.lurker.webconfig.exception.BusinessException;

public class JsonParseException extends BusinessException {

    public JsonParseException(Object data, Exception e) {
        super("json parse error", e);
        super.data = data;
    }
}

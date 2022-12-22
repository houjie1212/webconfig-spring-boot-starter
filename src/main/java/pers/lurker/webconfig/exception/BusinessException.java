package pers.lurker.webconfig.exception;

import org.springframework.http.HttpStatus;
import pers.lurker.webconfig.response.ReturnCodeEnum;

public class BusinessException extends RuntimeException {

    protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    protected String code = ReturnCodeEnum.FAILED.getCode();
    protected String message = ReturnCodeEnum.FAILED.getMsg();
    protected Object data;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BusinessException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, Object data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BusinessException setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public BusinessException setCode(String code) {
        this.code = code;
        return this;
    }

    public BusinessException setMessage(String message) {
        this.message = message;
        return this;
    }

    public BusinessException setData(Object data) {
        this.data = data;
        return this;
    }
}

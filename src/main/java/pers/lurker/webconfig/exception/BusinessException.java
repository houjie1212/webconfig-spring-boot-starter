package pers.lurker.webconfig.exception;

import org.springframework.http.HttpStatus;
import pers.lurker.webconfig.response.ReturnCodeEnum;

public class BusinessException extends RuntimeException {

    protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    protected String code = ReturnCodeEnum.FAILED.getCode();
    protected String message = ReturnCodeEnum.FAILED.getMsg();

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
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

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

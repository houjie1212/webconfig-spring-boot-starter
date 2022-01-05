package pers.lurker.webconfig.exception.ext;

import org.springframework.http.HttpStatus;
import pers.lurker.webconfig.exception.BusinessException;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

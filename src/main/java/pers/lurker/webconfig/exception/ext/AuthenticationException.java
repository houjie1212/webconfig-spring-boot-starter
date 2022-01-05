package pers.lurker.webconfig.exception.ext;

import org.springframework.http.HttpStatus;
import pers.lurker.webconfig.exception.BusinessException;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

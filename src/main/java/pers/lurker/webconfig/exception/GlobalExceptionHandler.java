package pers.lurker.webconfig.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pers.lurker.webconfig.filter.jsonparam.JsonParameterRequestHolder;
import pers.lurker.webconfig.response.R;
import pers.lurker.webconfig.response.ReturnCodeEnum;
import pers.lurker.webconfig.util.JsonUtil;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        printLog(request, ex, false);
        R<?> result = R.builder().buildFail(ReturnCodeEnum.FAILED.getCode(), ex.getMessage());
        return super.handleExceptionInternal(ex, result, headers, status, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<R<?>> methodArgumentTypeMismatchExceptionHandler(
            MethodArgumentTypeMismatchException e, WebRequest request) {
        printLog(request, e, false);
        R<?> result = R.builder().buildFail(ReturnCodeEnum.FAILED.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<?>> handleBusinessException(BusinessException ex, WebRequest request) {
        printLog(request, ex, ex.getStatus().is5xxServerError());
        R<?> result = R.builder().buildFail(ex.getCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(result);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        printLog(request, ex, false);
        R<?> result = R.builder().buildFail(ReturnCodeEnum.UNKNOWN_EXCEPTION.getCode(), ex.getMessage());
        return super.handleExceptionInternal(ex, result, headers, status, request);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<R<?>> exceptionHandler(Throwable e, WebRequest request) {
        printLog(request, e, true);
        R<?> result = R.builder().buildFail(ReturnCodeEnum.UNKNOWN_EXCEPTION.getCode(), ReturnCodeEnum.UNKNOWN_EXCEPTION.getMsg());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    private void printLog(WebRequest request, Throwable t, boolean printStrackTrace) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String requestMethod = servletWebRequest.getRequest().getMethod();
        String requestURL = servletWebRequest.getRequest().getRequestURL().toString();
        String contentType = servletWebRequest.getRequest().getContentType();
        String requestParameter;
        if ("GET".equalsIgnoreCase(requestMethod) || "DELETE".equalsIgnoreCase(requestMethod)) {
            requestParameter = JsonUtil.obj2String(servletWebRequest.getParameterMap());
        } else { // POST
            if ("application/json".equals(contentType)) {
                byte[] bytes = JsonParameterRequestHolder.getJsonParameter();
                Object o = JsonUtil.bytes2Obj(bytes, Object.class);
                requestParameter = JsonUtil.obj2String(o);
            } else {
                requestParameter = JsonUtil.obj2String(servletWebRequest.getParameterMap());
            }
        }

        if (printStrackTrace) {
            log.error(String.format("request error %s %s contentType: %s params: %s",
                    requestMethod, requestURL, contentType, requestParameter), t);
        } else {
            log.error(String.format("request error %s %s contentType: %s params: %s, reason: %s",
                    requestMethod, requestURL, contentType, requestParameter, t.getMessage()));
        }

    }

}

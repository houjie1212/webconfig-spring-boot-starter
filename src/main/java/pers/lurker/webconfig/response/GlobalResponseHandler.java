package pers.lurker.webconfig.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Set;

@RestControllerAdvice
@ConditionalOnProperty(value = "global.response.advice.enabled", havingValue = "true", matchIfMissing = true)
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Value("${global.response.advice.ignore-urls:}")
    private Set<String> ignoreUrls;
    @Autowired
    private HttpServletRequest request;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @PostConstruct
    public void postConstruct() {
        ignoreUrls.add("/**/actuator/**");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查注解@IgnorReponseAdvice是否存在，存在则忽略拦截
        if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        if (returnType.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 检查过滤请求路径
        String requestURI = request.getRequestURI();
        boolean anyMatch = ignoreUrls.stream().anyMatch(ig -> antPathMatcher.match(ig, requestURI));
        return !anyMatch;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (MediaType.TEXT_HTML_VALUE.equals(MediaType.toString(Collections.singleton(selectedContentType)))) {
            ServletServerHttpResponse ssResp = (ServletServerHttpResponse) response;
            ssResp.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }
        if (body instanceof R) {
            return body;
        }
        return R.builder().buildOk(body);
    }

}

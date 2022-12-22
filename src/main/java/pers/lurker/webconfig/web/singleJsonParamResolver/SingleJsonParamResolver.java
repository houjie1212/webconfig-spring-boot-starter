package pers.lurker.webconfig.web.singleJsonParamResolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pers.lurker.webconfig.exception.ext.BadRequestException;
import pers.lurker.webconfig.filter.jsonparam.JsonParameterRequestHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author houjie
 */
public class SingleJsonParamResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(SingleJsonParamResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SingleJsonParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();

        if (contentType == null || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            log.error("解析参数异常，contentType需为{}", MediaType.APPLICATION_JSON_VALUE);
            throw new BadRequestException("解析参数异常，contentType必须为application/json");
        }

        if (!"POST".equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("解析参数异常，请求类型必须为post");
            throw new BadRequestException("解析参数异常，请求类型必须为post");
        }
        return bindRequestParams(parameter);
    }

    private Object bindRequestParams(MethodParameter parameter) {
        SingleJsonParam singleJsonParam = parameter.getParameterAnnotation(SingleJsonParam.class);

        Class<?> parameterType = parameter.getParameterType();
        String requestBody = getRequestBody();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params;
        try {
            params = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new BadRequestException("参数解析异常");
        }

        params = CollectionUtils.isEmpty(params) ? new HashMap<>(0) : params;
        String name = StringUtils.hasText(singleJsonParam.value()) ? singleJsonParam.value() : parameter.getParameterName();
        Object value = params.get(name);

        if (singleJsonParam.required()) {
            if (StringUtils.isEmpty(value)) {
                log.error("参数" + name + "解析异常，required=true，值不能为空");
                throw new BadRequestException("参数" + name + "不能为空");
            }
        } else {
            if (value == null) {
                value = singleJsonParam.defaultValue();
            }
        }

        return ConvertUtils.convert(value, parameterType);
    }

    /**
     * 获取请求body
     *
     * @return 请求body
     */
    private String getRequestBody() {
        final byte[] jsonParameter = JsonParameterRequestHolder.getJsonParameter();
        return new String(jsonParameter, StandardCharsets.UTF_8);
    }
}

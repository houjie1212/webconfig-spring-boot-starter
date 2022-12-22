package pers.lurker.webconfig.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.lurker.webconfig.enumeration.BaseEnum;
import pers.lurker.webconfig.filter.jsonparam.JsonParameterFilter;
import pers.lurker.webconfig.filter.xss.XssFilterProperties;
import pers.lurker.webconfig.filter.xss.XssRequestFilter;
import pers.lurker.webconfig.serializer.RequestTZDateDeSerializer;
import pers.lurker.webconfig.serializer.RequestTZDateSerializer;
import pers.lurker.webconfig.web.interceptor.TimeZoneInterceptor;
import pers.lurker.webconfig.web.singleJsonParamResolver.SingleJsonParamResolver;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration("commonWebMvcConfiguration")
@EnableConfigurationProperties(XssFilterProperties.class)
public class WebConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfiguration.class);
    private final XssFilterProperties xssFilterProperties;

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormat;
    @Value("${global.response.advice.enabled:true}")
    private boolean globalResponseAdvice;

    public WebConfiguration(XssFilterProperties xssFilterProperties) {
        this.xssFilterProperties = xssFilterProperties;
    }

    @Bean
    public FilterRegistrationBean<JsonParameterFilter> jsonParameterFilterRegistration() {
        FilterRegistrationBean<JsonParameterFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JsonParameterFilter());
        registration.setOrder(0);
        return registration;
    }

    @Bean
    @ConditionalOnProperty(prefix = "xss.request", value = "enabled", havingValue = "true")
    public FilterRegistrationBean<XssRequestFilter> xssRequestFilterRegistration() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String source = p.getText().trim();
                // 把字符串做XSS过滤
                return StringEscapeUtils.escapeHtml4(source);
            }
        });
        objectMapper.registerModule(simpleModule);

        FilterRegistrationBean<XssRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssRequestFilter(xssFilterProperties.getRequest(), objectMapper));
        registration.setName("xssFilter");
        if (!CollectionUtils.isEmpty(xssFilterProperties.getRequest().getPatterns())) {
            registration.setUrlPatterns(xssFilterProperties.getRequest().getPatterns());
        }
        registration.setOrder(1);
        return registration;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 解决返回String类型数据转型错误问题
        converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BaseEnum.class, new EnumSerializer(BaseEnum.class));

        DateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        //時間序列化
        RequestTZDateSerializer dateSerializer = new RequestTZDateSerializer(Boolean.FALSE,simpleDateFormat);
        simpleModule.addSerializer(Date.class,dateSerializer);
        //時間反序列化
        DateDeserializers.DateDeserializer base =  DateDeserializers.DateDeserializer.instance;
        simpleModule.addDeserializer(Date.class,new RequestTZDateDeSerializer(base,simpleDateFormat,dateFormat));
        simpleModule.addDeserializer(Date.class,new DateDeserializer(new DateConverter()));

        if (StringUtils.hasText(dateFormat)) {
            objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        }

        if (xssFilterProperties.getResponse().isEnabled()) {
            simpleModule.addSerializer(String.class, new JsonSerializer<String>() {
                @Override
                public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(StringEscapeUtils.escapeHtml4(value));
                }
            });
        }
        objectMapper.registerModule(simpleModule);
        converter.setObjectMapper(objectMapper);
        if (!globalResponseAdvice) {
            converters.add(converter);
            return;
        }

        ArrayList<MediaType> supportMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportMediaTypes.add(MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(supportMediaTypes);
        converters.add(0, converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new TimeZoneInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
        registry.addConverterFactory(new EnumConverterFactory());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SingleJsonParamResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}

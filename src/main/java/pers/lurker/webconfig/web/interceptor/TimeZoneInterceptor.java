package pers.lurker.webconfig.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import pers.lurker.webconfig.support.Contants;
import pers.lurker.webconfig.util.ThreadLocalTransmittableUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TimeZone;

/**
 * 时区拦截器
 * 不同时区客户端请求根据请求时区参数保存不同的时区至ThreadLocal中
 * @author hongwei.peng
 * @version 1.0
 */
public class TimeZoneInterceptor implements HandlerInterceptor {

    private static final String TIME_ZONE_PARAM_NAME = "_tz";
    private static final String DEFAULT_TIME_ZONE_ID = "GMT+8";
    private static final Logger logger = LoggerFactory.getLogger(TimeZoneInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
//        String queryString = request.getQueryString();
//        String input = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
//        logger.debug(input);
//        if(!StringUtils.hasText(queryString)) return true;
//        logger.debug("get請求參數：{}",queryString);
//        String[] queryParameters = queryString.split("&");
//        Map<String,String> queryParamMap = new HashMap<>();
//        Arrays.stream(queryParameters).forEach((paramEntry)->{
//            String[] keyValue = paramEntry.split("=");
//            String key = keyValue[0];
//            String value = keyValue[1];
//            queryParamMap.put(key,value);
//        });
//        String zoneId = queryParamMap.get(TIME_ZONE_PARAM_NAME);
        String zoneId = request.getParameter(TIME_ZONE_PARAM_NAME);
        if(!StringUtils.hasText(zoneId)) {
            ThreadLocalTransmittableUtil.set(Contants.REQ_TIME_ZONE,TimeZone.getTimeZone(DEFAULT_TIME_ZONE_ID));
            ThreadLocalTransmittableUtil.set(Contants.REQ_TIME_ZONE_ID,DEFAULT_TIME_ZONE_ID);
            return true;
        }
//        TimeZone.getAvailableIDs();
        logger.debug("request zoneId:{}",zoneId);
        TimeZone requestTimeZone = TimeZone.getTimeZone(zoneId);
        ThreadLocalTransmittableUtil.set(Contants.REQ_TIME_ZONE_ID,zoneId);
        ThreadLocalTransmittableUtil.set(Contants.REQ_TIME_ZONE,requestTimeZone);
        logger.debug("requestTimeZone:{}",requestTimeZone);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        logger.trace("remove request Timezone...");
        ThreadLocalTransmittableUtil.remove(Contants.REQ_TIME_ZONE);
        ThreadLocalTransmittableUtil.remove(Contants.REQ_TIME_ZONE_ID);
    }
}

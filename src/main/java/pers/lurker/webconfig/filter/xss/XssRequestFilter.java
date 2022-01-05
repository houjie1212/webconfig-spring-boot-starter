package pers.lurker.webconfig.filter.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * xss请求过滤器，转义请求中的参数
 */
public class XssRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(XssRequestFilter.class);

    private final List<String> excludes;
    private final ObjectMapper objectMapper;

    public XssRequestFilter(XssFilterProperties.Request xssRequestProperties, ObjectMapper objectMapper) {
        log.info("初始化xss过滤器");
        this.excludes = xssRequestProperties.getExcludes();
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (excludes != null && excludes.size() > 0) {
            if (excludes.stream().anyMatch(pattern -> antPathMatcher.match(pattern, servletPath))) {
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(new XssHttpServletRequestWrapper(request, objectMapper), response);
            }
        } else {
            filterChain.doFilter(new XssHttpServletRequestWrapper(request, objectMapper), response);
        }
    }

}

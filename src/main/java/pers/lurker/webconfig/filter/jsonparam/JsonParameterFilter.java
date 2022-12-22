package pers.lurker.webconfig.filter.jsonparam;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonParameterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getContentType() != null
                && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            filterChain.doFilter(new JsonParameterHttpServletRequestWrapper(request), response);
            JsonParameterRequestHolder.removeJsonParameter();
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

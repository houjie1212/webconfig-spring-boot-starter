package pers.lurker.webconfig.filter.xss;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "xss")
public class XssFilterProperties {

    private Request request = new Request();
    private Response response = new Response();

    public static class Request {
        private boolean enabled = false;
        private List<String> patterns = new ArrayList<>();
        private List<String> excludes = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public Request setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public List<String> getPatterns() {
            return patterns;
        }

        public Request setPatterns(List<String> patterns) {
            this.patterns = patterns;
            return this;
        }

        public List<String> getExcludes() {
            return excludes;
        }

        public Request setExcludes(List<String> excludes) {
            this.excludes = excludes;
            return this;
        }
    }

    public static class Response {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public Response setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
    }

    public Request getRequest() {
        return request;
    }

    public XssFilterProperties setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Response getResponse() {
        return response;
    }

    public XssFilterProperties setResponse(Response response) {
        this.response = response;
        return this;
    }
}

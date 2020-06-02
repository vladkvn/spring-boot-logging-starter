package com.filtering.starters.logging;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CustomRequestResponseLoggingFilter extends AbstractRequestLoggingFilter {

    List<String> paths;

    PathMatcher pathMatcher = new AntPathMatcher();

    public CustomRequestResponseLoggingFilter(RequestResponseLoggingProperties aggregationRequestResponseLoggingProperties){
        setAfterMessagePrefix(aggregationRequestResponseLoggingProperties.getAfterMessagePrefix());
        setAfterMessageSuffix(aggregationRequestResponseLoggingProperties.getAfterMessageSuffix());
        setBeforeMessagePrefix(aggregationRequestResponseLoggingProperties.getBeforeMessagePrefix());
        setBeforeMessageSuffix(aggregationRequestResponseLoggingProperties.getBeforeMessageSuffix());
        setIncludeClientInfo(aggregationRequestResponseLoggingProperties.isIncludeClientInfo());
        setIncludeHeaders(aggregationRequestResponseLoggingProperties.isIncludeHeaders());
        setIncludePayload(aggregationRequestResponseLoggingProperties.isIncludePayload());
        setIncludeQueryString(aggregationRequestResponseLoggingProperties.includeQueryString);
        setMaxPayloadLength(aggregationRequestResponseLoggingProperties.getMaxPayloadLength());
        paths = aggregationRequestResponseLoggingProperties.getPaths();
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return paths.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}
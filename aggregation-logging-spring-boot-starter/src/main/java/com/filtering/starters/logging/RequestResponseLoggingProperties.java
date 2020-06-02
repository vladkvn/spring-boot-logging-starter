package com.filtering.starters.logging;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "filter.logging")
public class RequestResponseLoggingProperties {

    @Value("${includeQueryString:true}")
    public boolean includeQueryString;

    @Value("${afterMessagePrefix:---------------------request end---------------------}")
    private String afterMessagePrefix;

    @Value("${afterMessageSuffix:-----------------------------------------------------}")
    private String afterMessageSuffix;

    @Value("${beforeMessagePrefix:---------------------request start---------------------}")
    private String beforeMessagePrefix;

    @Value("${beforeMessageSuffix:-----------------------------------------------------}")
    private String beforeMessageSuffix;

    @Value("${includeClientInfo:false}")
    private boolean includeClientInfo;

    @Value("${includeHeaders:true}")
    private boolean includeHeaders;

    @Value("${includePayload:true}")
    private boolean includePayload;

    @Value("${maxPayloadLength:2048}")
    private int maxPayloadLength;

    @Value("${paths:/**}")
    private List<String> paths;
}

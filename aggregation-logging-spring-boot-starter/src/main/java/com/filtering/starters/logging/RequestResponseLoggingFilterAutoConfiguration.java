package com.filtering.starters.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(CustomRequestResponseLoggingFilter.class)
@EnableConfigurationProperties(RequestResponseLoggingProperties.class)
public class RequestResponseLoggingFilterAutoConfiguration {
    Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilterAutoConfiguration.class);

    @Autowired
    RequestResponseLoggingProperties aggregationRequestResponseLoggingProperties;

    @Bean
    @ConditionalOnMissingBean
    CustomRequestResponseLoggingFilter requestResponseLoggingFilter()
    {
        logger.info("AggregationRequestResponseLoggingFilter bean created!");
        return new CustomRequestResponseLoggingFilter(aggregationRequestResponseLoggingProperties);
    }
}

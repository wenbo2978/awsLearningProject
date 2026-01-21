package com.aws_practice.aws.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppAwsProperties.class)
public class PropsConfig {
}

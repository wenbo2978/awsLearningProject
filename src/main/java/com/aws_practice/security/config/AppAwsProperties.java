package com.aws_practice.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="aws")
public record AppAwsProperties(String region, S3Props s3) {
    public record S3Props(String bucket) {}
}

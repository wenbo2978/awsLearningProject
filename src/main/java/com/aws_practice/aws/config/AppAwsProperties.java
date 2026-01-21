package com.aws_practice.aws.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="aws")
public record AppAwsProperties(String region, S3Props s3) {
    public record S3Props(String bucket) {}
}

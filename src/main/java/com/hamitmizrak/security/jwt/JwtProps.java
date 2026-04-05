package com.hamitmizrak.security.jwt;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;


@ConfigurationProperties(prefix = "app.jwt")
public record JwtProps(String secret, Duration expire) { }

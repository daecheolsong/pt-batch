package com.example.ptbatch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kakaotalk")
public class KakaoMessageConfig {
    private String host;
    private String token;
    private String url;
}

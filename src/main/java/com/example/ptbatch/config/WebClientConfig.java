package com.example.ptbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final KakaoMessageConfig config;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(config.getHost())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setBearerAuth(config.getToken());
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }
}

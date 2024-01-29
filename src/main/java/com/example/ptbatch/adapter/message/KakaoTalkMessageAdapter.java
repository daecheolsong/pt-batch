package com.example.ptbatch.adapter.message;

import com.example.ptbatch.config.KakaoMessageConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Objects;

/**
 * @author daecheol song
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class KakaoTalkMessageAdapter {

    private final WebClient webClient;

    public boolean sendKakaoTalkMessage(String uuid, String text) {
        KakaoTalkMessageResponse response = webClient.post().uri("/v1/api/talk/friends/message/default/send")
                .body(BodyInserters.fromValue(KakaoTalkMessageRequest.of(uuid, text)))
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        ClientResponse::createException
                )
                .bodyToMono(KakaoTalkMessageResponse.class)
                .block();


        if (Objects.isNull(response) || Objects.isNull(response.getSuccessfulReceiverUuids())) {
            return false;
        }

        return !response.getSuccessfulReceiverUuids().isEmpty();
    }

}

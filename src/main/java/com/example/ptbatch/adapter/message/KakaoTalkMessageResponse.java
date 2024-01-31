package com.example.ptbatch.adapter.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
@Value
public class KakaoTalkMessageResponse {
    @JsonProperty("successful_receiver_uuids")
    List<String> successfulReceiverUuids;
}

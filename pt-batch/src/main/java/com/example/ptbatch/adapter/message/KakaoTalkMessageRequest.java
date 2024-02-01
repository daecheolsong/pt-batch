package com.example.ptbatch.adapter.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */

public class KakaoTalkMessageRequest {

    @JsonProperty("template_object")
    private final TemplateObject templateObject;

    @JsonProperty("receiver_uuids")
    private final List<String> receiverUuids;


    private KakaoTalkMessageRequest(TemplateObject templateObject, List<String> receiverUuids) {
        this.templateObject = templateObject;
        this.receiverUuids = receiverUuids;
    }

    public static KakaoTalkMessageRequest of(String uuid, String text) {
        TemplateObject.Link link = TemplateObject.Link.of("");
        TemplateObject templateObject = TemplateObject.of("text", text, link);
        List<String> receiverUuids = Collections.singletonList(uuid);
        return new KakaoTalkMessageRequest(templateObject, receiverUuids);
    }

    @Value(staticConstructor = "of")
    public static class TemplateObject {
        @JsonProperty("object_type")
        String objectType;
        String text;
        Link link;

        @Value(staticConstructor = "of")
        public static class Link {
            @JsonProperty("web_url")
            String webUrl;
        }
    }
}

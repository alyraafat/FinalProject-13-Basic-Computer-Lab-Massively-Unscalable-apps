package com.example.miniapp.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DeliveryChannel {
    @JsonProperty("email")
    EMAIL,

    @JsonProperty("push")
    PUSH,

    @JsonProperty("none")
    NONE;

    public static DeliveryChannel fromString(String value) {
        for (DeliveryChannel channel : values()) {
            if (channel.name().equalsIgnoreCase(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Unknown delivery channel: " + value);
    }
}
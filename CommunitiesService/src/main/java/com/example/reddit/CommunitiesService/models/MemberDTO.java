package com.example.reddit.CommunitiesService.models;

import java.util.UUID;

public class MemberDTO {
    private UUID id;

    public MemberDTO() {}
    public MemberDTO(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

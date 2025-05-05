package com.redditclone.user_service.dtos;

import com.redditclone.user_service.models.User;
import lombok.Data;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private boolean activated;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setActivated(user.isActivated());
        return dto;
    }
}

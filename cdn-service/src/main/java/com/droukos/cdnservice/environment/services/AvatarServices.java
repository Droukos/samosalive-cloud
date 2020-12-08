package com.droukos.cdnservice.environment.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AvatarServices {
    PUT_AVATAR_PIC("put_avatar_pic/{id}");

    private final String url;

    public String getFullUrl() {
        return "api/cdn/" + this.getUrl();
    }
}

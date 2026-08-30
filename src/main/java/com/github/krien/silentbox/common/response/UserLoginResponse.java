package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String token;

    public UserLoginResponse(String token){
        this.token = token;
    }
}

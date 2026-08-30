package com.github.krien.silentbox.common.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String name;
    private String username;
    private String password;
}

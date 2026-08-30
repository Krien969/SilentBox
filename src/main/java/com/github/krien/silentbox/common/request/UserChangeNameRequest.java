package com.github.krien.silentbox.common.request;

import lombok.Data;

@Data
public class UserChangeNameRequest {

    private String username;
    private String password;
    private String name;

}

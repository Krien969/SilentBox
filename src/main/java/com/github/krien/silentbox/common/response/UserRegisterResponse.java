package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class UserRegisterResponse {

    private Long id;
    private String username;
    private String name;

    public UserRegisterResponse(Long id, String username, String name){
        this.id = id;
        this.username = username;
        this.name = name;
    }

}

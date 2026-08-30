package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class UserSelfResponse {

    private Long id;
    private String name;
    private String username;
    private boolean isAdmin;
    private String createTime;

}

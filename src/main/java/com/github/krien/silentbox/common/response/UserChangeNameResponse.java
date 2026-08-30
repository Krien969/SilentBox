package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class UserChangeNameResponse {

    private String oldName;
    private String newName;
    private Long userId;

}

package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class FileUploadMultResponse {

    private Long fromUserId;
    private String filename;
    private String name;
    private String url;
    private String createTime;
    private String type;
    private String saveType;
    private String key;
    private Long id;

}

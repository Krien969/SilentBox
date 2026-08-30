package com.github.krien.silentbox.common.request;

import lombok.Data;

@Data
public class FileUploadNoTokenRequest {

    private String filename;
    private String key;

}

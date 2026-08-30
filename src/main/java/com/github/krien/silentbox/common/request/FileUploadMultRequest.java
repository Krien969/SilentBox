package com.github.krien.silentbox.common.request;

import lombok.Data;

@Data
public class FileUploadMultRequest {

    private String saveType;
    private String filename;
    private String key;

}

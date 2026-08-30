package com.github.krien.silentbox.common.response;

import lombok.Data;

@Data
public class FileGetMaxSizeResponse {

    private Long maxSize;
    private String maxSizeMB;

}

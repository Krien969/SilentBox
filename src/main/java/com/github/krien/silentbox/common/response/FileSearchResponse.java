package com.github.krien.silentbox.common.response;

import com.github.krien.silentbox.entities.UploadFile;
import lombok.Data;

import java.util.List;

@Data
public class FileSearchResponse {

    private List<UploadFile> files;
    private long total;

}

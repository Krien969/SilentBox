package com.github.krien.silentbox.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.github.krien.silentbox.common.FileSaveTypeEnum;
import com.github.krien.silentbox.common.SResult;
import com.github.krien.silentbox.common.response.FileUploadPublicResponse;
import com.github.krien.silentbox.entities.UploadFile;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.services.UploadFileService;
import com.github.krien.silentbox.services.impl.UploadFileServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class FileSaveUtil {

    @Resource
    private PathProperties pathProperties;

    public String getFileUrl(@NotNull String filename, @NotNull FileSaveTypeEnum type){
        return switch (type) {
            case FileSaveTypeEnum.PUBLIC -> "/file/public/" + filename;
            case FileSaveTypeEnum.PROTECT -> "/file/protect/" + filename;
            case FileSaveTypeEnum.NO_TOKEN -> "/file/protect/no-token/" + filename;
        };
    }

    public boolean isRightFileName(String filename){
        return filename != null && !filename.startsWith(".") && !filename.endsWith(".");
    }

    public String getFileType(String rawFileName){
        return StringUtils.getFilenameExtension(rawFileName);
    }

    public String getFormatFileName(String filename, Long userId){
        String extension = this.getFileType(filename);
        return userId.toString() + "_" + UUID.randomUUID().toString().replace("-", "") + (extension == null ? "" : "." + extension);
    }

    public File getSaveFile(String filename, FileSaveTypeEnum type){
        String p = switch (type){
            case FileSaveTypeEnum.PUBLIC -> pathProperties.getPublicPath();
            case FileSaveTypeEnum.PROTECT -> pathProperties.getProtectPath();
            case FileSaveTypeEnum.NO_TOKEN -> pathProperties.getNoTokenPath();
        };
        return Paths.get(System.getProperty("user.dir"), p, filename).toFile();
    }

}

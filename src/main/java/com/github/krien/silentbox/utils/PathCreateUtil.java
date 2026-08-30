package com.github.krien.silentbox.utils;

import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

@Component
@Slf4j
public class PathCreateUtil implements CommandLineRunner {

    @Resource
    private PathProperties pathProperties;

    public boolean createPath(){
        String basePath = System.getProperty("user.dir");
        File publicSavePath = new File(basePath, pathProperties.getPublicPath());
        File protectSavePath = new File(basePath, pathProperties.getProtectPath());
        File noTokenProtectSavePath= new File(basePath, pathProperties.getNoTokenPath());
        boolean publicSavePathExist = true;
        boolean protectSavePathExist = true;
        boolean noTokenProtectSavePathExist = true;
        if (!publicSavePath.exists()){
            publicSavePathExist = publicSavePath.mkdirs();
        }
        if (!protectSavePath.exists()){
            protectSavePathExist = protectSavePath.mkdirs();
        }
        if (!noTokenProtectSavePath.exists()){
            noTokenProtectSavePathExist = noTokenProtectSavePath.mkdirs();
        }
        ArrayList<String> errorArr = new ArrayList<>();
        if (!publicSavePathExist){
            errorArr.add("save/public");
        }
        if (!protectSavePathExist){
            errorArr.add("save/protect");
        }
        if (!noTokenProtectSavePathExist){
            errorArr.add("save/protect/no-token");
        }
        return errorArr.isEmpty();
    }

    @Override
    public void run(String... args) throws Exception {
        boolean createSuccessful = this.createPath();
        if (!createSuccessful){
            throw new IllegalStateException("上传路径不存在且创建失败");
        }
        log.info("文件上传路径存在/创建成功");
    }
}

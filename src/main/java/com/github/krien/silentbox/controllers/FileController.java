package com.github.krien.silentbox.controllers;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.krien.silentbox.common.FileSaveTypeEnum;
import com.github.krien.silentbox.common.SResult;
import com.github.krien.silentbox.common.request.FileUploadCommonRequest;
import com.github.krien.silentbox.common.request.FileUploadMultRequest;
import com.github.krien.silentbox.common.request.FileUploadNoTokenRequest;
import com.github.krien.silentbox.common.response.*;
import com.github.krien.silentbox.entities.UploadFile;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.services.UploadFileService;
import com.github.krien.silentbox.services.UserService;
import com.github.krien.silentbox.utils.FileSaveUtil;
import com.github.krien.silentbox.utils.PathProperties;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class    FileController {

    @Resource
    private UploadFileService uploadFileService;

    @Resource
    private UserService userService;

    @Resource
    private SResult sResult;

    @Resource
    private PathProperties pathProperties;

    @Resource
    private FileSaveUtil fileSaveUtil;

    @Value("${upload.max-size}")
    private Long maxSize;

    @GetMapping("/get-max-size")
    @SaCheckLogin
    public SResult fileGetMaxSize(){
        FileGetMaxSizeResponse response = new FileGetMaxSizeResponse();
        response.setMaxSize(maxSize);
        long readLong = maxSize / 1_000_000;
        response.setMaxSizeMB(Long.toString(readLong));
        return sResult.success("获取文件大小上限成功", response);
    }

    @PostMapping("/upload/public")
    @SaCheckLogin
    public SResult fileUploadPublic(@RequestPart("file") MultipartFile file, @RequestPart("detail") FileUploadCommonRequest request){
        if (file.getSize() > maxSize) return sResult.fail(413, "上传文件过大");
        if (!fileSaveUtil.isRightFileName(file.getOriginalFilename())) return sResult.fail(413, "文件名不合法");
        Long userId = StpUtil.getLoginIdAsLong();
        User target = userService.getById(userId);
        if (target == null) return sResult.fail(404, "上传文件的用户不存在");
        UploadFile newFile = new UploadFile();
        newFile.setName(request.getFilename());
        newFile.setKey(null);
        newFile.setFromUserId(userId);
        String ext = Optional.ofNullable(fileSaveUtil.getFileType(file.getOriginalFilename())).orElse("none");
        newFile.setType(ext);
        String formatFileName = fileSaveUtil.getFormatFileName(file.getOriginalFilename(), userId);
        newFile.setFileName(formatFileName);
        newFile.setSaveType("public");
        String savePath = fileSaveUtil.getFileUrl(formatFileName, FileSaveTypeEnum.PUBLIC);
        newFile.setUrl(savePath);
        File saveFile = fileSaveUtil.getSaveFile(formatFileName, FileSaveTypeEnum.PUBLIC);
        SResult result = this.doSave(file, newFile, saveFile);
        if (result != null) return result;
        FileUploadPublicResponse response = new FileUploadPublicResponse();
        response.setFromUserId(newFile.getFromUserId());
        response.setType(newFile.getType());
        response.setName(request.getFilename());
        response.setSaveType(newFile.getSaveType());
        response.setCreateTime(newFile.getCreateTime());
        response.setUrl(newFile.getUrl());
        response.setFilename(newFile.getFileName());
        response.setId(newFile.getId());
        return sResult.success("公共文件上传成功", response);
    }

    @PostMapping("/upload/protect")
    @SaCheckLogin
    public SResult fileUploadProtect(@RequestPart("file") MultipartFile file, @RequestPart("detail") FileUploadCommonRequest request){
        if (file.getSize() > maxSize) return sResult.fail(413, "上传文件过大");
        if (!fileSaveUtil.isRightFileName(file.getOriginalFilename())) return sResult.fail(413, "文件名不合法");
        Long userId = StpUtil.getLoginIdAsLong();
        User target = userService.getById(userId);
        if (target == null) return sResult.fail(404, "上传文件的用户不存在");
        UploadFile newFile = new UploadFile();
        newFile.setName(request.getFilename());
        newFile.setKey(null);
        newFile.setFromUserId(userId);
        String ext = Optional.ofNullable(fileSaveUtil.getFileType(file.getOriginalFilename())).orElse("none");
        newFile.setType(ext);
        String formatFileName = fileSaveUtil.getFormatFileName(file.getOriginalFilename(), userId);
        newFile.setFileName(formatFileName);
        newFile.setSaveType("protect");
        String savePath = fileSaveUtil.getFileUrl(formatFileName, FileSaveTypeEnum.PROTECT);
        newFile.setUrl(savePath);
        File saveFile = fileSaveUtil.getSaveFile(formatFileName, FileSaveTypeEnum.PROTECT);
        SResult result = this.doSave(file, newFile, saveFile);
        if (result != null) return result;
        FileUploadPublicResponse response = new FileUploadPublicResponse();
        response.setFromUserId(newFile.getFromUserId());
        response.setType(newFile.getType());
        response.setName(request.getFilename());
        response.setSaveType(newFile.getSaveType());
        response.setCreateTime(newFile.getCreateTime());
        response.setUrl(newFile.getUrl());
        response.setFilename(newFile.getFileName());
        response.setId(newFile.getId());
        return sResult.success("受保护文件上传成功", response);
    }

    @PostMapping("/upload/no-token")
    @SaCheckLogin
    public SResult fileUploadNoToken(@RequestPart("file") MultipartFile file, @RequestPart("detail") FileUploadNoTokenRequest request){
        if (file.getSize() > maxSize) return sResult.fail(413, "上传文件过大");
        if (!fileSaveUtil.isRightFileName(file.getOriginalFilename())) return sResult.fail(413, "文件名不合法");
        Long userId = StpUtil.getLoginIdAsLong();
        User target = userService.getById(userId);
        if (target == null) return sResult.fail(404, "上传文件的用户不存在");
        if (request.getKey() == null || request.getKey().isEmpty()) return sResult.fail(400, "no-token保存类型必须传入key");
        UploadFile newFile = new UploadFile();
        newFile.setName(request.getFilename());
        newFile.setKey(request.getKey());
        newFile.setFromUserId(userId);
        String ext = Optional.ofNullable(fileSaveUtil.getFileType(file.getOriginalFilename())).orElse("none");
        newFile.setType(ext);
        String formatFileName = fileSaveUtil.getFormatFileName(file.getOriginalFilename(), userId);
        newFile.setFileName(formatFileName);
        newFile.setSaveType("no-token");
        String savePath = fileSaveUtil.getFileUrl(formatFileName, FileSaveTypeEnum.NO_TOKEN);
        newFile.setUrl(savePath);
        File saveFile = fileSaveUtil.getSaveFile(formatFileName, FileSaveTypeEnum.NO_TOKEN);
        SResult result = this.doSave(file, newFile, saveFile);
        if (result != null) return result;
        FileUploadPublicResponse response = new FileUploadPublicResponse();
        response.setFromUserId(newFile.getFromUserId());
        response.setType(newFile.getType());
        response.setName(request.getFilename());
        response.setSaveType(newFile.getSaveType());
        response.setCreateTime(newFile.getCreateTime());
        response.setUrl(newFile.getUrl());
        response.setFilename(newFile.getFileName());
        response.setId(newFile.getId());
        return sResult.success("受保护-非鉴权文件上传成功", response);
    }

    private SResult doSave(MultipartFile file, UploadFile uploadFile, File saveFile) {
        boolean dbSaved = uploadFileService.save(uploadFile);
        if (!dbSaved) {
            boolean exists = saveFile.exists() && saveFile.delete();
            return sResult.fail(
                    500,
                    "文件数据录入失败，文件存在状态：" + (exists ? "是" : "否")
            );
        }
        try {
            saveFile.getParentFile().mkdirs();
            file.transferTo(saveFile);
        } catch (IOException e) {
            uploadFileService.removeById(uploadFile.getId());
            return sResult.fail(500, "文件上传失败");
        }
        return null;
    }

    @GetMapping("/list/self")
    @SaCheckLogin
    public SResult fileListSelf(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "public") String saveType){
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        if (user == null) return sResult.fail(404, "用户不存在");
        if (!List.of("public", "protect", "no-token").contains(saveType)) return sResult.fail(400, "saveType为不存在的类型");
        Page<UploadFile> data = uploadFileService.lambdaQuery()
                .select(
                        UploadFile::getFileName,
                        UploadFile::getId,
                        UploadFile::getName,
                        UploadFile::getFromUserId,
                        UploadFile::getType,
                        UploadFile::getSaveType,
                        UploadFile::getUrl,
                        UploadFile::getCreateTime,
                        UploadFile::getKey
                )
                .eq(UploadFile::getFromUserId, userId)
                .eq(UploadFile::getSaveType, saveType)
                .page(new Page<>(pageNum, pageSize));
        return sResult.success("查询当前用户上传的文件列表成功", data.getRecords());
    }

    @GetMapping("/list/public")
    @SaCheckLogin
    public SResult fileListPublic(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){
        Page<UploadFile> pg = new Page<>(pageNum, pageSize);
        Page<UploadFile> data = uploadFileService.lambdaQuery()
                .select(
                        UploadFile::getFileName,
                        UploadFile::getId,
                        UploadFile::getName,
                        UploadFile::getFromUserId,
                        UploadFile::getType,
                        UploadFile::getSaveType,
                        UploadFile::getUrl,
                        UploadFile::getCreateTime
                )
                .eq(UploadFile::getSaveType, "public")
                .page(pg);
        return sResult.success("查询公共文件列表成功", data.getRecords());
    }

    @GetMapping("/download/{id}")
    @SaCheckLogin
    public Object fileDownload(@PathVariable("id") Long id, HttpServletResponse response, @RequestParam(defaultValue = "") String key) throws Exception {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        if (user == null) return sResult.fail(404, "下载用户不存在");
        UploadFile target = uploadFileService.getById(id);
        if (target == null) return sResult.fail(404, "下载文件不存在");
        if (!target.getSaveType().equals("public")) {
            if (target.getSaveType().equals("no-token") && !target.getKey().equals(key))
                return sResult.fail(403, "key错误，无法下载文件");
            if (!target.getFromUserId().equals(userId)) return sResult.fail(403, "无权下载该文件");
        }
        response.setContentType("application/octet-stream");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" +
                        URLEncoder.encode(target.getType().equals("无") ? target.getName() : target.getName() + "." + target.getType(), StandardCharsets.UTF_8)
        );
        FileSaveTypeEnum t;
        try {
            t = switch (target.getSaveType()) {
                case "public" -> FileSaveTypeEnum.PUBLIC;
                case "protect" -> FileSaveTypeEnum.PROTECT;
                case "no-token" -> FileSaveTypeEnum.NO_TOKEN;
                default -> throw new IllegalStateException("非法的文件保存类型");
            };
        } catch (IllegalStateException e) {
            return sResult.fail(500, e.getMessage());
        }
        Files.copy(fileSaveUtil.getSaveFile(target.getFileName(), t).toPath(), response.getOutputStream());
        response.flushBuffer();
        return null;
    }

    @PostMapping("/upload/mult")
    @SaCheckLogin
    public SResult fileUploadMult(@RequestPart("files") List<MultipartFile> files, @RequestPart("detail")List<FileUploadMultRequest> requests){
        if (files.size() != requests.size()) return sResult.fail(400, "文件数量与元数据数量不一致");
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        if (user == null) return sResult.fail(404, "上传用户不存在");
        ArrayList<FileUploadMultResponse> arr = new ArrayList<>();
        for (int index = 0; index < files.size(); index ++){
            MultipartFile file = files.get(index);
            FileUploadMultRequest request = requests.get(index);
            FileSaveTypeEnum t;
            try {
                t = switch (request.getSaveType()){
                    case "public" -> FileSaveTypeEnum.PUBLIC;
                    case "protect" -> FileSaveTypeEnum.PROTECT;
                    case "no-token" -> FileSaveTypeEnum.NO_TOKEN;
                    default -> throw new IllegalStateException("非法的保存类型");
                };
                if (t.equals(FileSaveTypeEnum.NO_TOKEN) && request.getKey().isEmpty()) throw new IllegalStateException("no-token保存类型必须传入key");
            } catch (IllegalStateException e){
                return sResult.fail(400, e.getMessage());
            }
            UploadFile newFile = new UploadFile();
            newFile.setSaveType(request.getSaveType());
            newFile.setFromUserId(userId);
            newFile.setKey(t.equals(FileSaveTypeEnum.NO_TOKEN) ? request.getKey() : null);
            String ext = Optional.ofNullable(fileSaveUtil.getFileType(file.getOriginalFilename())).orElse("无");
            newFile.setType(ext);
            newFile.setFileName(fileSaveUtil.getFormatFileName(file.getOriginalFilename(), userId));
            newFile.setName(request.getFilename());
            newFile.setUrl(fileSaveUtil.getFileUrl(newFile.getFileName(), t));
            SResult result = this.doSave(file, newFile, fileSaveUtil.getSaveFile(newFile.getFileName(), t));
            if (result != null) return result;
            FileUploadMultResponse response = new FileUploadMultResponse();
            response.setCreateTime(newFile.getCreateTime());
            response.setKey(newFile.getKey());
            response.setName(newFile.getName());
            response.setType(ext);
            response.setUrl(newFile.getUrl());
            response.setFilename(newFile.getFileName());
            response.setFromUserId(newFile.getFromUserId());
            response.setSaveType(request.getSaveType());
            response.setId(newFile.getId());
            arr.add(response);
        }
        return sResult.success("多文件保存成功", arr);
    }

    @GetMapping("/get-info")
    @SaCheckLogin
    public SResult fileGetCount(){
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        if (user == null) return sResult.fail(404, "用户不存在");
        long allFilesTotal = uploadFileService.count();
        long publicFilesTotal = uploadFileService.lambdaQuery()
                .eq(UploadFile::getSaveType, "public")
                .count();
        long selfFilesTotal = uploadFileService.lambdaQuery()
                .eq(UploadFile::getFromUserId, userId)
                .count();
        long selfPublicTotal = uploadFileService.lambdaQuery()
                .eq(UploadFile::getFromUserId, userId)
                .eq(UploadFile::getSaveType, "public")
                .count();
        long selfProtectTotal = uploadFileService.lambdaQuery()
                .eq(UploadFile::getFromUserId, userId)
                .eq(UploadFile::getSaveType, "protect")
                .count();
        long selfNoTokenTotal = uploadFileService.lambdaQuery()
                .eq(UploadFile::getFromUserId, userId)
                .eq(UploadFile::getSaveType, "no-token")
                .count();
        FileGetInfoResponse response = new FileGetInfoResponse();
        response.setTotal(allFilesTotal);
        response.setSelfTotal(selfFilesTotal);
        response.setSelfPublicTotal(selfPublicTotal);
        response.setSelfProtectTotal(selfProtectTotal);
        response.setSelfNoTokenTotal(selfNoTokenTotal);
        response.setPublicTotal(publicFilesTotal);
        return sResult.success("获取文件寄存模块详情信息成功", response);
    }

    @SaCheckLogin
    @GetMapping("/search")
    public SResult searchFile(@RequestParam String word, @RequestParam(defaultValue = "public") String saveType){
        long userId = StpUtil.getLoginIdAsLong();
        if (!List.of("public", "protect", "no-token").contains(saveType)) return sResult.fail(400, "不合法的保存类型");
        List<UploadFile> files;
        try{
            files = switch (saveType){
                case "public" -> uploadFileService.lambdaQuery()
                        .eq(UploadFile::getSaveType, "public")
                        .like(UploadFile::getName, word)
                        .list();
                case "protect" -> uploadFileService.lambdaQuery()
                        .eq(UploadFile::getFromUserId, userId)
                        .eq(UploadFile::getSaveType, "protect")
                        .like(UploadFile::getName, word)
                        .list();
                case "no-token" -> uploadFileService.lambdaQuery()
                        .eq(UploadFile::getFromUserId, userId)
                        .eq(UploadFile::getSaveType, "no-token")
                        .like(UploadFile::getName, word)
                        .list();
                default -> throw new IllegalStateException("查询时出现未知错误");
            };
        } catch (IllegalStateException e){
            return sResult.fail(500, e.getMessage());
        }
        FileSearchResponse response = new FileSearchResponse();
        response.setFiles(files);
        response.setTotal(files.toArray().length);
        return sResult.success("搜索成功", response);
    }

}

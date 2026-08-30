package com.github.krien.silentbox.services.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.github.krien.silentbox.entities.UploadFile;
import com.github.krien.silentbox.mappers.UploadFileMapper;
import com.github.krien.silentbox.services.UploadFileService;
import org.springframework.stereotype.Service;

@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {
}

package com.github.krien.silentbox.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.krien.silentbox.entities.UploadFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFile> {
}

package com.github.krien.silentbox.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.krien.silentbox.entities.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

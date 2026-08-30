package com.github.krien.silentbox.services.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.mappers.UserMapper;
import com.github.krien.silentbox.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

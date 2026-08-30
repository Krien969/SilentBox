package com.github.krien.silentbox.services.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.github.krien.silentbox.entities.Post;
import com.github.krien.silentbox.mappers.PostMapper;
import com.github.krien.silentbox.services.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
}

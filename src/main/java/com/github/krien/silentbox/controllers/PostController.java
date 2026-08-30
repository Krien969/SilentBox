package com.github.krien.silentbox.controllers;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.krien.silentbox.common.SResult;
import com.github.krien.silentbox.common.request.PostUploadRequest;
import com.github.krien.silentbox.common.response.PostUploadResponse;
import com.github.krien.silentbox.entities.Post;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.services.PostService;
import com.github.krien.silentbox.services.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/post")
@RestController
@Slf4j
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private SResult sResult;

    @SaCheckLogin
    @PostMapping("/upload")
    public SResult postUpload(@RequestBody PostUploadRequest request){
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        if (user == null) return sResult.fail(404, "用户不存在");
        Post target = new Post();
        target.setContent(request.getContent());
        target.setTitle(request.getTitle());
        target.setFromUserId(user.getId());
        target.setFromUserName(user.getName());
        boolean isSave = postService.save(target);
        if (!isSave) return sResult.fail(500, "上传文章数据时数据库错误");
        PostUploadResponse response = new PostUploadResponse();
        response.setId(target.getId());
        response.setTitle(target.getTitle());
        response.setCreateTime(target.getCreateTime());
        return sResult.success("文章上传成功", response);
    }

    @SaCheckLogin
    @GetMapping("/get/list")
    public SResult postGetList(@RequestParam(defaultValue = "1") Long pageNum, @RequestParam(defaultValue = "10") Long pageSize){
        List<Post> posts = postService.list(new Page<>(pageNum, pageSize));
        return sResult.success("查询文章列表成功", posts);
    }

    @SaCheckLogin
    @GetMapping("/get/{id}")
    public SResult postGetOne(@PathVariable Long id){
        Post target = postService.getById(id);
        if (target == null) return sResult.fail(404, "ID为" + id.toString() + "的文章不存在");
        return sResult.success("查找文章成功", target);
    }
}

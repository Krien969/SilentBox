package com.github.krien.silentbox.controllers;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.github.krien.silentbox.common.SResult;
import com.github.krien.silentbox.common.request.UserChangeNameRequest;
import com.github.krien.silentbox.common.request.UserLoginRequest;
import com.github.krien.silentbox.common.request.UserRegisterRequest;
import com.github.krien.silentbox.common.response.*;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.services.UserService;
import com.github.krien.silentbox.utils.PasswordUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Resource
    private SResult sResult;

    @Resource
    private UserService userService;

    @Resource
    private PasswordUtil passwordUtil;

    @SaIgnore
    @PostMapping("/login")
    public SResult userLogin(@RequestBody UserLoginRequest request){
        User user = userService.lambdaQuery()
                .eq(User::getUsername, request.getUsername())
                .one();
        if (user == null) return sResult.fail(404, "用户不存在");
        if (!passwordUtil.match(request.getPassword(), user.getPassword())) return sResult.fail(401, "密码错误");
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        return sResult.success("登录成功", new UserLoginResponse(token));
    }

    @SaIgnore
    @PostMapping("/register")
    public SResult userRegister(@RequestBody UserRegisterRequest request){
        boolean hasUser = userService.lambdaQuery()
                .eq(User::getUsername, request.getUsername())
                .or()
                .eq(User::getName, request.getName())
                .exists();
        if (hasUser) return sResult.fail(409, "用户名或昵称已存在");
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setName(request.getName());
        newUser.setAdmin(0);
        newUser.setPassword(passwordUtil.encode(request.getPassword()));
        boolean hasEnter = userService.save(newUser);
        if (!hasEnter) return sResult.fail(500, "用户注册数据写入失败");
        return sResult.success("注册成功", new UserRegisterResponse(newUser.getId(), newUser.getUsername(), newUser.getName()));
    }

    @SaCheckLogin
    @GetMapping("/self")
    public SResult userSelf(){
        Long userId = StpUtil.getLoginIdAsLong();
        User target = userService.getById(userId);
        if (target == null) return sResult.fail(404, "ID " + userId.toString() + " 用户不存在");
        UserSelfResponse response = new UserSelfResponse();
        response.setId(target.getId());
        response.setName(target.getName());
        response.setUsername(target.getUsername());
        response.setAdmin(target.getAdmin() == 1);
        response.setCreateTime(target.getCreateTime());
        return sResult.success("获取个人信息成功", response);
    }

    @SaCheckLogin
    @PostMapping("/change/name")
    public SResult userChangeName(@RequestBody UserChangeNameRequest request){
        boolean hasName = userService.lambdaQuery()
                .eq(User::getName, request.getName())
                .exists();
        if (hasName) return sResult.fail(409, "该昵称已存在");
        Long userId = StpUtil.getLoginIdAsLong();
        User target = userService.getById(userId);
        if (target == null) return sResult.fail(404, "ID " + userId + " 用户不存在");
        String oldName = target.getName();
        if (!target.getUsername().equals(request.getUsername())){
            return sResult.fail(401, "用户名错误");
        } else if (!passwordUtil.match(request.getPassword(), target.getPassword())){
            return sResult.fail(401, "密码错误");
        }
        target.setName(request.getName());
        boolean hasEnter = userService.updateById(target);
        if (!hasEnter) return sResult.fail(500, "用户更改昵称失败，数据无法更新");
        UserChangeNameResponse response = new UserChangeNameResponse();
        response.setUserId(userId);
        response.setNewName(target.getName());
        response.setOldName(oldName);
        return sResult.success("用户更改昵称成功", response);
    }

    @GetMapping("/detail/{id}")
    @SaCheckLogin
    public SResult getUserDetail(@PathVariable long id){
        User user = userService.getById(id);
        if (user == null) return sResult.fail(404, "用户不存在");
        UserDetailResponse response = new UserDetailResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setUsername(user.getUsername());
        return sResult.success("获取用户信息成功", response);
    }

}

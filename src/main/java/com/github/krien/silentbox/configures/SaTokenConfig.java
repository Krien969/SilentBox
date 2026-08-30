package com.github.krien.silentbox.configures;

import cn.dev33.satoken.stp.StpInterface;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.services.UserService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SaTokenConfig implements StpInterface {

    @Resource
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        User target = userService.getById(Long.valueOf(o.toString()));
        if (target == null){
            return Collections.emptyList();
        }
        List<String> roles = new ArrayList<>();
        roles.add("user");
        if (target.getAdmin() == 1){
            roles.add("admin");
        }
        return roles;
    }
}

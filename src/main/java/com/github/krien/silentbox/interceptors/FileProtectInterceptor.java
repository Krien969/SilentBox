package com.github.krien.silentbox.interceptors;

import cn.dev33.satoken.stp.StpUtil;
import com.github.krien.silentbox.entities.UploadFile;
import com.github.krien.silentbox.entities.User;
import com.github.krien.silentbox.services.UploadFileService;
import com.github.krien.silentbox.services.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class FileProtectInterceptor implements HandlerInterceptor {

    @Resource
    private UploadFileService uploadFileService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String filename = uri.substring(uri.lastIndexOf("/") + 1);
        if (filename.isEmpty()) return false;
        UploadFile target = uploadFileService.lambdaQuery()
                .eq(UploadFile::getFileName, filename)
                .one();
        if (target == null) return false;

        if (uri.startsWith("/file/protect/no-token/")){
            String key = request.getParameter("key");
            if (key == null || key.isEmpty()) return false;
            return target.getKey().equals(key);
        }

        if (uri.startsWith("/file/protect/")){
            StpUtil.checkLogin();
            Long userId = StpUtil.getLoginIdAsLong();
            return userId.equals(target.getFromUserId());
        }

        return true;
    }
}

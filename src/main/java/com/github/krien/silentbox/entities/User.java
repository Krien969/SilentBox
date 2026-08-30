package com.github.krien.silentbox.entities;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "昵称不能为空")
    private String name;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @TableField(value = "admin", fill = FieldFill.INSERT)
    private Integer admin;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private String updateTime;

}

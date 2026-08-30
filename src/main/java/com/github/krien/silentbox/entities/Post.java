package com.github.krien.silentbox.entities;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TableName("post")
public class Post {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "title 字段不能为空")
    private String title;

    @TableField("from_user_id")
    private Long fromUserId;

    @NotBlank(message = "content 字段不能为空")
    private String content;

    @TableField("from_user_name")
    private String fromUserName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;

}

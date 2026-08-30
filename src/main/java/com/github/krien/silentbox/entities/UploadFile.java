package com.github.krien.silentbox.entities;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("upload_file")
public class UploadFile {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("from_user_id")
    private Long fromUserId;

    @TableField("save_type")
    private String saveType;

    @NotBlank(message = "文件展示名不能为空")
    private String name;

    @NotBlank(message = "文件名不能为空")
    @TableField("file_name")
    private String fileName;

    @NotNull(message = "文件类型不能为空")
    private String type;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;

    @NotBlank(message = "文件的访问URL不能为空")
    private String url;

    @TableField("`key`")
    private String key;

}

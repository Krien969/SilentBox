package com.github.krien.silentbox.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("upload.path")
@Data
public class PathProperties {

    private String publicPath;
    private String protectPath;
    private String noTokenPath;

}

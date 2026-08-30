package com.github.krien.silentbox.common;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SResult {

    private Integer code;
    private String message;
    private Object data;

    public SResult success(String message){
        SResult s = new SResult();
        s.setCode(200);
        s.setMessage(message);
        s.setData(null);
        return s;
    }

    public SResult success(String message, Object response){
        SResult s = new SResult();
        s.setCode(200);
        s.setMessage(message);
        s.setData(response);
        return s;
    }

    public SResult fail(Integer code, String message){
        SResult s = new SResult();
        s.setCode(code);
        s.setMessage(message);
        s.setData(null);
        return s;
    }

    public SResult fail(Integer code, String message, Object response){
        SResult s = new SResult();
        s.setCode(code);
        s.setData(response);
        s.setMessage(message);
        return s;
    }

}

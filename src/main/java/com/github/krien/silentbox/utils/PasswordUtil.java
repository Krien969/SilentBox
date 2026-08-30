package com.github.krien.silentbox.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final int BCRYPT_COST;

    public PasswordUtil(){
        this.BCRYPT_COST = 12;
    }

    public String encode(String pd){
        return BCrypt.hashpw(pd, BCrypt.gensalt(BCRYPT_COST));
    }

    public boolean match(String pd, String spd){
        return BCrypt.checkpw(pd, spd);
    }

}

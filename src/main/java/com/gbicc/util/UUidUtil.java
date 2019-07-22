package com.gbicc.util;

import java.util.UUID;

public class UUidUtil {  
    public static String getUuid(){  
        UUID uuid = UUID.randomUUID();  
        String uuidStr =  uuid.toString();  
        uuidStr = uuidStr.replace("-", "");  
        return uuidStr;  
    }  
      
    public static void main(String[] args) {  
        System.out.println(getUuid());
    }  
  
}
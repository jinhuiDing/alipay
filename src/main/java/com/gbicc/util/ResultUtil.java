package com.gbicc.util;


import com.gbicc.enums.ResultEnums;
import com.gbicc.vo.AppletResult;
import org.springframework.stereotype.Component;

@Component
public class ResultUtil {
    public static AppletResult success(ResultEnums resultEnums, Object object){

        AppletResult result = new AppletResult();
        result.setCode(resultEnums.getCode());
        result.setMsg(resultEnums.getMsg());
        result.setData(object);
        return result;
    }

    public static AppletResult success(){
        AppletResult result = new AppletResult();
        result.setCode(ResultEnums.RETURN_SUCCESS.getCode());
        result.setMsg(ResultEnums.RETURN_SUCCESS.getMsg());
        return result;
    }

    public static AppletResult errorSeponse(int code, String msg){
        AppletResult result = new AppletResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


    public static AppletResult error(ResultEnums resultEnums, String...msg){
        AppletResult result = new AppletResult();
        result.setCode(resultEnums.getCode());
        if (msg.length>0){
            StringBuffer sb=new StringBuffer(msg[0]);
            result.setMsg(sb.append(resultEnums.getMsg()).toString());
        }else {
            result.setMsg(resultEnums.getMsg());
        }
        return result;
    }

}

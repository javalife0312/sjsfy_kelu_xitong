package sjs.fy.opt.api.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by lulijun on 16/3/8.
 */
public class CommonResult {

    private int code;
    private String msg;
    private Object data;
    private Object threader;

    public static CommonResult success(String msg) {
        return CommonResult.build(0, msg,"{}","{}");
    }
    public static CommonResult fail(String msg) {
        return CommonResult.build(-1, msg,"{}","{}");
    }

    public static CommonResult success(Object data,Object title){
        return CommonResult.build(0, "success",data,title);
    }
    public static CommonResult success(Object data){
        return CommonResult.build(0, "success",data,"{}");
    }

    private static CommonResult build(int code, String msg, Object data,Object threader) {
        CommonResult r = new CommonResult();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        r.setThreader(threader);
        return r;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("code", this.code);
        json.put("msg", this.msg);
        json.put("data", this.data);
        json.put("titles",this.threader);
        return json;
    }


    public void setCode(int code) {
        this.code = code;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public void setThreader(Object threader) {
        this.threader = threader;
    }
}

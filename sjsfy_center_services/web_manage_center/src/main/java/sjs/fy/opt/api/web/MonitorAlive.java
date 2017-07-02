package sjs.fy.opt.api.web;

import sjs.fy.opt.api.util.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lulijun on 16/5/27.
 */
@Controller
public class MonitorAlive {
    @RequestMapping(value = "/api/monitor/alive", produces = "application/json;charset=utf8")
    @ResponseBody
    public Object alive() {
        return CommonResult.success("success").toJson();
    }
}

package sjs.fy.opt.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jinguowei on 16/1/13.
 */

@Controller
public class DefaultController {
    /**
     * 根据页面url和显示类型返回引用指标和预显示指标
     * @return
     */
    @RequestMapping(value="/",method= RequestMethod.GET, produces = "application/json;charset=utf8")
    @ResponseBody
    public ModelAndView index(){
        return new ModelAndView("redirect:AdminLTE/pages/admin/admin_center_manage.html");
    }
}

package sjs.fy.opt.api.web.admin;

import sjs.fy.opt.api.service.DBService;
import sjs.fy.opt.api.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 16-12-3.
 */
@Controller
public class UserManageController {

    @Autowired
    DBService dbService;

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/user/listusers", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listusers() {

        String sql = "select id,username,password,showname,status from sjsfy_opt_shipin.sjsfy_kelu_userinfo";
        List<Map<String,Object>> data = dbService.listInfos(sql);

        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        generateTitle(titles,"id","ID");
        generateTitle(titles,"username","用户名");
        generateTitle(titles,"password","密码");
        generateTitle(titles,"showname","中文名");
        generateTitle(titles,"status","状态");
        generateTitle(titles,"","操作");
        System.out.println(CommonResult.success(data,titles).toJson().toJSONString());
        return CommonResult.success(data,titles).toJson().toJSONString();
    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/user/listusers_id", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listusers_id(String id) {

        String sql = "select id,username,password,showname,status from sjsfy_opt_shipin.sjsfy_kelu_userinfo where id="+id;
        List<Map<String,Object>> data = dbService.listInfos(sql);
        System.out.println(CommonResult.success(data).toJson().toJSONString());
        return CommonResult.success(data).toJson().toJSONString();
    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/user/listusers_delete_id", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listusers_delete_id(String id) {
        String sql = "delete from sjsfy_opt_shipin.sjsfy_kelu_userinfo where id="+id;
        dbService.executeSql(sql);
        System.out.println(CommonResult.success("success").toJson().toJSONString());
        return CommonResult.success("success").toJson().toJSONString();
    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/user/save_or_update", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listusers_id(String id,String username,String password,String showname,String status) {
        String sql = "";

        if(id==null || "".equals(id)){
            sql = "INSERT INTO sjsfy_opt_shipin.sjsfy_kelu_userinfo values(null,'"+username+"','"+password+"','"+showname+"',"+status+")";
        }else{
            sql = "update sjsfy_opt_shipin.sjsfy_kelu_userinfo set username='"+username+"',password='"+password+"',showname='"+showname+"',status="+status+" where id="+id;
        }
        System.out.println(sql);
        dbService.executeSql(sql);
        System.out.println(CommonResult.success("success").toJson().toJSONString());
        return CommonResult.success("success").toJson().toJSONString();
    }


    /**
     * 拼接生成Titles
     * @param titles
     * @param data
     * @param title
     */
    private void generateTitle(List<Map<String,String>> titles,String data,String title){
        Map<String,String> map = new HashMap<String,String>();
        map.put("data",data);
        map.put("title",title);
        titles.add(map);
    }
}

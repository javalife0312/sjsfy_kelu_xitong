package sjs.fy.opt.api.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sjs.fy.opt.api.service.DBService;
import sjs.fy.opt.api.util.CommonResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 16-12-3.
 */
@Controller
public class CenterManageController {

    @Autowired
    DBService dbService;

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/center/listinfos", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listusers() {

        String sql = "select * from sjsfy_opt_shipin.sjsfy_kelu_manageinfo order by id";
        List<Map<String,Object>> data = dbService.listInfos(sql);

        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        generateTitle(titles,"id","ID");
        generateTitle(titles,"fating_desc","法庭描述");
        generateTitle(titles,"faguan_code","法官Code");
        generateTitle(titles,"faguan_name","法官名字");
        generateTitle(titles,"shexiangji_ip","摄像机IP");
        generateTitle(titles,"shexiangji_desc","摄像机描述");
        generateTitle(titles,"shexiangji_username","摄像机账号");
        generateTitle(titles,"shexiangji_password","摄像机密码");
        generateTitle(titles,"shexiangji_zhuangtai","摄像机状态");
        generateTitle(titles,"diannaozhuji_ip","电脑主机IP");
        generateTitle(titles,"diannaozhuji_panfu","电脑主机盘符");
        generateTitle(titles,"","操作");
        System.out.println(CommonResult.success(data,titles).toJson().toJSONString());
        return CommonResult.success(data,titles).toJson().toJSONString();
    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/center/list_by_id", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object list_by_id(String id) {
        String sql = "select * from sjsfy_opt_shipin.sjsfy_kelu_manageinfo where id="+id;
        List<Map<String,Object>> data = dbService.listInfos(sql);
        System.out.println(CommonResult.success(data).toJson().toJSONString());
        return CommonResult.success(data).toJson().toJSONString();
    }

//    /**
//     * 列表显示帐号信息
//     * @return
//     */
//    @RequestMapping(value = "/admin/center/listusers_delete_id", produces = "application/json;charset=utf8",method = RequestMethod.GET)
//    @ResponseBody
//    public Object listusers_delete_id(String id) {
//        String sql = "delete from sjsfy_opt_shipin.sjsfy_kelu_userinfo where id="+id;
//        dbService.executeSql(sql);
//        System.out.println(CommonResult.success("success").toJson().toJSONString());
//        return CommonResult.success("success").toJson().toJSONString();
//    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "/admin/center/save_or_update", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object save_or_update(String id,
                                 String fating_desc,
                                 String faguan_code,
                                 String faguan_name,
                                 String shexiangji_ip,
                                 String shexiangji_desc,
                                 String shexiangji_username,
                                 String shexiangji_password,
                                 String shexiangji_zhuangtai,
                                 String diannaozhuji_ip,
                                 String diannaozhuji_panfu) {
        String sql = "";
        if(id==null || "".equals(id)){
            sql = "INSERT INTO sjsfy_opt_shipin.sjsfy_kelu_manageinfo values(null,'"+fating_desc+"','"+faguan_code+"','"+faguan_name+"','"+shexiangji_ip+"','"+shexiangji_desc+"','"+shexiangji_username+"','"+shexiangji_password+"',"+shexiangji_zhuangtai+",'"+diannaozhuji_ip+"','"+diannaozhuji_panfu+"')";
        }else{
            sql = "update sjsfy_opt_shipin.sjsfy_kelu_manageinfo set " +
                    "fating_desc='"+fating_desc+"'," +
                    "faguan_code='"+faguan_code+"'," +
                    "faguan_name='"+faguan_name+"'," +

                    "shexiangji_ip='"+shexiangji_ip+"'," +
                    "shexiangji_desc='"+shexiangji_desc+"'," +
                    "shexiangji_username='"+shexiangji_username+"'," +
                    "shexiangji_password='"+shexiangji_password+"'," +
                    "shexiangji_zhuangtai='"+shexiangji_zhuangtai+"'," +
                    "diannaozhuji_ip='"+diannaozhuji_ip+"'," +
                    "diannaozhuji_panfu='"+diannaozhuji_panfu+"'" +
                    "where id="+id;
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

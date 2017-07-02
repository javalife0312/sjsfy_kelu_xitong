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
@RequestMapping("/admin/room/")
public class RoomManageController {

    @Autowired
    DBService dbService;

    /**
     * 获取信息
     * @return
     */
    @RequestMapping(value = "list", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object list() {

        String sql = "select id,room_host,room_desc,room_type,parent_room_id,device_host,device_username,device_password from sjsfy_opt_shipin.sjsfy_kelu_roominfo;";
        System.out.println(sql);
        List<Map<String,Object>> data = dbService.listInfos(sql);

        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        generateTitle(titles,"id","ID");
        generateTitle(titles,"room_host","审判厅IP");
        generateTitle(titles,"room_desc","审判厅描述");
        generateTitle(titles,"room_type","节点类型");
        generateTitle(titles,"parent_room_id","父节点");
        generateTitle(titles,"device_host","摄像机IP");
        generateTitle(titles,"device_username","摄像机用户名");
        generateTitle(titles,"device_password","摄像机密码");
        generateTitle(titles,"","操作");

        System.out.println(CommonResult.success(data,titles).toJson().toJSONString());
        return CommonResult.success(data,titles).toJson().toJSONString();
    }

    /**
     * 获取信息
     * @return
     */
    @RequestMapping(value = "list_id", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object list_id(String id) {

        String sql = "select id,room_host,room_desc,room_type,parent_room_id,device_host,device_username,device_password from sjsfy_opt_shipin.sjsfy_kelu_roominfo where id="+id;
        System.out.println(sql);
        List<Map<String,Object>> data = dbService.listInfos(sql);

        System.out.println(CommonResult.success(data).toJson().toJSONString());
        return CommonResult.success(data).toJson().toJSONString();
    }

    /**
     * 更新 节点信息
     * @return
     */
    @RequestMapping(value = "update", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object update(String id,String room_host,String room_desc,String room_type,String device_host,String device_username,String device_password) {
        String sql = "update sjsfy_opt_shipin.sjsfy_kelu_roominfo set " +
                "room_host='"+room_host+"'," +
                "room_desc='"+room_desc+"'," +
                "room_type="+room_type+"," +
                "device_host='"+device_host+"'," +
                "device_username='"+device_username+"', " +
                "device_password='"+device_password+"' " +
                "where id=" + id;
        dbService.executeSql(sql);
        System.out.println(CommonResult.success("success").toJson().toJSONString());
        return CommonResult.success("success").toJson().toJSONString();
    }

    /**
     * 显示信息
     * @return
     */
    @RequestMapping(value = "save", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object save(String parent_room_id,String room_host,String room_desc,String room_type,String device_host,String device_username,String device_password) {
        String sql = "INSERT INTO sjsfy_opt_shipin.sjsfy_kelu_roominfo values(null,'"+room_host+"','"+room_desc+"',"+room_type+","+parent_room_id+",'"+device_host+"','"+device_username+"','"+device_password+"')";
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

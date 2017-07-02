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
@RequestMapping("/admin/device/")
public class DeviceManageController {

    @Autowired
    DBService dbService;

    /**
     * 获取信息
     * @return
     */
    @RequestMapping(value = "list", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object list() {

        String sql = "select id,device_host,device_desc,device_type,parent_device_id,status from sjsfy_opt_shipin.sjsfy_kelu_deviceinfo";
        System.out.println(sql);
        List<Map<String,Object>> data = dbService.listInfos(sql);

        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        generateTitle(titles,"id","ID");
        generateTitle(titles,"device_host","设备IP");
        generateTitle(titles,"device_desc","设备描述");
        generateTitle(titles,"device_type","节点类型");
        generateTitle(titles,"parent_device_id","设备父节点");
        generateTitle(titles,"status","状态");
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

        String sql = "select id,device_host,device_desc,device_type,parent_device_id,status from sjsfy_opt_shipin.sjsfy_kelu_deviceinfo where id="+id;
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
    public Object update(String id,String device_type,String status,String device_host,String device_desc) {
        String sql = "update sjsfy_opt_shipin.sjsfy_kelu_deviceinfo set device_host='"+device_host+"',device_desc='"+device_desc+"',status="+status+",device_type="+device_type+" where id=" + id;
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
    public Object save(String parent_device_id,String status,String device_host,String device_desc,String device_type) {
        String sql = "INSERT INTO sjsfy_opt_shipin.sjsfy_kelu_deviceinfo values(null,'"+device_host+"','"+device_desc+"',"+device_type+","+parent_device_id+","+status+")";
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

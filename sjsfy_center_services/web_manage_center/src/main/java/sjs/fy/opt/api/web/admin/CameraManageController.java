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
@RequestMapping("/admin/camera/")
public class CameraManageController {

    @Autowired
    DBService dbService;
    /**
     * 刻录信息统计
     * @return
     */
    @RequestMapping(value = "tongji", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object tongji() {

        String sql = "select " +
                "count(case when 1=1 then 1 end) as total," +
                "count(case when sjs_status=0 then 1 end) as offline, " +
                "count(case when sjs_status=1 then 1 end) as online, " +
                "count(case when sjs_status=2 then 1 end) as sleeping " +
            "from " +
                "sjsfy_opt_shipin.sjsfy_device_camera";
        System.out.println(sql);
        List<Map<String,Object>> data = dbService.listInfos(sql);

        System.out.println(CommonResult.success(data).toJson().toJSONString());
        return CommonResult.success(data).toJson().toJSONString();
    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "listcamera", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listcamera(String tongji_tag) {
        String sql  = "select " +
                    "sjs_deviceIP," +
                    "sjs_cameraLocation," +
                    "sjs_name," +
                    "sjs_cameraStatus," +
                    "sjs_deviceIP " +
                "from " +
                    "sjsfy_opt_shipin.sjsfy_device_camera";
        if(tongji_tag.equals("total")){}
        if(tongji_tag.equals("offline")){
            sql += " where sjs_status=0";
        }
        if(tongji_tag.equals("online")){
            sql += " where sjs_status=1";
        }
        if(tongji_tag.equals("sleeping")){
            sql += " where sjs_status=2";
        }

        List<Map<String,Object>> data = dbService.listInfos(sql);
        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        generateTitle(titles,"sjs_deviceIP","设备IP");
        generateTitle(titles,"sjs_cameraLocation","设备位置");
        generateTitle(titles,"sjs_name","设备名字");
        generateTitle(titles,"sjs_cameraStatus","设备状态");
        generateTitle(titles,"sjs_deviceIP","操作信息");
        System.out.println(CommonResult.success(data,titles).toJson().toJSONString());
        return CommonResult.success(data,titles).toJson().toJSONString();
    }

    /**
     * 显示信息
     * @return
     */
    @RequestMapping(value = "listcamera_ip", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listcamera_ip(String sjs_deviceIP) {

        String sql =
                "select " +
                    "sjs_deviceIP," +
                    "sjs_deviceUsrname," +
                    "sjs_devicePwd," +
                    "sjs_deviceFaguan " +
                "from " +
                    "sjsfy_opt_shipin.sjsfy_device_camera " +
                 "where sjs_deviceIP='"+sjs_deviceIP+"'";
        List<Map<String,Object>> data = dbService.listInfos(sql);
        System.out.println(CommonResult.success(data).toJson().toJSONString());
        return CommonResult.success(data).toJson().toJSONString();
    }

    /**
     * 显示信息
     * @return
     */
    @RequestMapping(value = "update", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object update(String sjs_deviceIP,String sjs_deviceUsrname,String sjs_devicePwd,String sjs_deviceFaguan) {

        String sql = "update sjsfy_opt_shipin.sjsfy_device_camera set " +
                "sjs_deviceFaguan='"+sjs_deviceFaguan+"'," +
                "sjs_deviceUsrname='"+sjs_deviceUsrname+"'," +
                "sjs_devicePwd='"+sjs_devicePwd+"' " +
                "where sjs_deviceIP='"+sjs_deviceIP+"'";
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

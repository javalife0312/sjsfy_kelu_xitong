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
@RequestMapping("/admin/kelu/")
public class KeluManageController {

    @Autowired
    DBService dbService;

    private String data_base = "sjsfy_opt_shipin";

    /**
     * 刻录信息统计
     * @return
     */
    @RequestMapping(value = "tongji", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object tongji() {

        String sql = "select " +
                    "count(case when 1=1 then 1 end) as total," +
                    "count(case when status=-1 then 1 end) as fail, " +
                    "count(case when status=1 then 1 end) as success, " +
                    "count(case when status not in(-1,1) then 1 end) as doing " +
                "from " +
                    data_base + ".sjsfy_kelu_keluinfo";
        System.out.println(sql);
        List<Map<String,Object>> data = dbService.listInfos(sql);

        System.out.println(CommonResult.success(data).toJson().toJSONString());
        return CommonResult.success(data).toJson().toJSONString();
    }

    /**
     * 列表显示帐号信息
     * @return
     */
    @RequestMapping(value = "listkelu", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listkelu(String tongji_tag) {
        String sql =
                "select \n" +
                    "\tkelu.id,\n" +
                    "\tkelu.room_host,\n" +
                    "\tkelu.device_host,\n" +
//                    "\tkelu.luxiang_start,\n" +
                    "\tkelu.luxiang_qujian,\n" +
//                    "\tkelu.luxiang_end,\n" +
//                    "\tkelu.kelu_start,\n" +
//                    "\tkelu.kelu_end,\n" +
                    "\tkelu.faguan,\n" +
                    "\tkelu.anjianbianhao,\n" +
                    "\tkelu.anyou,\n" +
                    "\tkelu.status\n" +
                "from \n" +
                "\t"+data_base+".sjsfy_kelu_keluinfo kelu\n";
        if(tongji_tag.equals("success")){
            sql += " where kelu.status=1";
        }
        if(tongji_tag.equals("fail")){
            sql += " where kelu.status=-1";
        }
        if(tongji_tag.equals("doing")){
            sql += " where kelu.status not in(1,-1)";
        }
        List<Map<String,Object>> data = dbService.listInfos(sql);
        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        generateTitle(titles,"id","ID");
        generateTitle(titles,"room_host","法庭IP");
        generateTitle(titles,"device_host","设备IP");

//        generateTitle(titles,"luxiang_start","录像开始时间");
        generateTitle(titles,"luxiang_qujian","录像区间");
//        generateTitle(titles,"luxiang_end","录像结束时间");
//        generateTitle(titles,"kelu_start","刻录开始时间");
//        generateTitle(titles,"kelu_end","刻录结束时间");
        generateTitle(titles,"faguan","法官账号");
        generateTitle(titles,"anjianbianhao","案件编号");
        generateTitle(titles,"anyou","案由");
        generateTitle(titles,"status","状态");
        generateTitle(titles,"","操作");
        System.out.println(CommonResult.success(data,titles).toJson().toJSONString());
        return CommonResult.success(data,titles).toJson().toJSONString();
    }

    /**
     * 显示信息
     * @return
     */
    @RequestMapping(value = "listkelu_id", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listkelu_id(String id) {

        String sql = "select id,device_host,luxiang_start,luxiang_end,kelu_start,kelu_end,status from "+data_base+".sjsfy_kelu_keluinfo where id="+id;
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
    public Object update(String id,String status) {

        String sql = "update "+data_base+".sjsfy_kelu_keluinfo set status="+status+" where id="+id;
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

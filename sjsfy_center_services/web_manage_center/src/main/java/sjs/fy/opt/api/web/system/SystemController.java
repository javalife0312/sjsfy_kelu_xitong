package sjs.fy.opt.api.web.system;

import sjs.fy.opt.api.service.DBService;
import sjs.fy.opt.api.service.ReportService;
import sjs.fy.opt.api.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lulijun on 16/5/27.
 * 系统操作的一些信息 包含三部分操作
 *
 * 1、报表的添删改查
 * 2、报表的图表管理
 * 3、图表的操作信息
 *
 * select chart_id,chart_type from waimai_data_agilebi.agilebi_report_chart where page_id=22
 * select id,chart_id,opt_type,opt_sql from waimai_data_agilebi.agilebi_report_option where chart_id=2
 * select * from waimai_data_agilebi.agilebi_report_chart_meta where chart_id=2
 */
@Controller
@Component
public class SystemController {

    @Autowired
    DBService dbService;
    @Autowired
    ReportService reportService;


    private Map<String,Object> getMap(String code,String title){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",code);
        map.put("title",title);
        return map;
    }
    /**
     * 列出系统报表
     * @return
     */
    @RequestMapping(value = "/sys/listReport", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listReport() {
        String sql = "select page_id,page_name,page_desc,page_url,page_owner from waimai_data_agilebi.agilebi_report_info";
        List<Map<String,Object>> data =  dbService.listInfos(sql);
        List<Map<String,Object>> titiles = new ArrayList<Map<String,Object>>();
        titiles.add(getMap("page_id","报表ID"));
        titiles.add(getMap("page_name","报表名字"));
        titiles.add(getMap("page_desc","报表报表描述"));
        titiles.add(getMap("page_url","报表短连接"));
        titiles.add(getMap("page_owner","报表责任人"));
        return CommonResult.success(data,titiles);
    }

    /**
     * 列出系统报表
     * @return
     */
    @RequestMapping(value = "/sys/listChart", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listChart(String page_id) {
        String sql = "select chart_id,chart_name,chart_desc,page_id from waimai_data_agilebi.agilebi_chart_info where page_id="+page_id;
        List<Map<String,Object>> data =  dbService.listInfos(sql);
        List<Map<String,Object>> titiles = new ArrayList<Map<String,Object>>();
        titiles.add(getMap("chart_id","图表ID"));
        titiles.add(getMap("chart_name","图表名字"));
        titiles.add(getMap("chart_desc","图表描述"));
        titiles.add(getMap("page_id","报表ID"));
        return CommonResult.success(data,titiles);
    }

    /**
     * 列出系统报表
     * @return
     */
    @RequestMapping(value = "/sys/listChartOption", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object listChartOption(String chart_id) {
        String sql = "select id,chart_id,opt_type,opt_sql,opt_title from waimai_data_agilebi.agilebi_report_chart_option where chart_id="+chart_id;
        List<Map<String,Object>> data =  dbService.listInfos(sql);
        List<Map<String,Object>> titiles = new ArrayList<Map<String,Object>>();
        titiles.add(getMap("id","操作ID"));
        titiles.add(getMap("chart_id","图表ID"));
        titiles.add(getMap("opt_type","操作类型"));
        titiles.add(getMap("opt_sql","操作的Sql"));
        titiles.add(getMap("opt_title","操作的表头"));
        return CommonResult.success(data,titiles);
    }

    /**
     * 操作图表的信息
     * @param sql
     * @param opt_type
     * @param page_id
     * @param chart_id
     * @return
     */
    @RequestMapping(value = "/sys/optChart", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object optChart(String sql,int opt_type,String page_id,String chart_id) {
        if(opt_type==1){//插入
            Long insert_chart_id = dbService.insertAndGetKey(sql);
            dbService.executeSql("insert into waimai_data_agilebi.agilebi_report_chart values("+insert_chart_id+","+page_id+")");
        }else if(opt_type==2){//修改
            dbService.executeSql(sql);
        }else if(opt_type==3){//删除
            dbService.executeSql(sql);
            dbService.executeSql("delete from waimai_data_agilebi.agilebi_report_chart where chart_id="+chart_id+" and page_id="+page_id+"");
        }
        return CommonResult.success("success");
    }
}

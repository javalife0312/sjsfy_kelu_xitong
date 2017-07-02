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
public class ChartController {

    @Autowired
    DBService dbService;
    @Autowired
    ReportService reportService;


    /**
     * 获取报表的默认显示
     * @param uid
     * @param page_id
     * @return
     */
    @RequestMapping(value = "/chart/pageOptions", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object pageOptions(String uid,String page_id) {
        String sql = "select chart_id,chart_type,chart_type_num from waimai_data_agilebi.agilebi_chart_info where onload_flag=1 and page_id="+page_id;
        List<Map<String,Object>> data = dbService.listInfos(sql);
        return CommonResult.success(data,null);
    }

    /**
     * 列出系统报表
     * @return
     */
    @RequestMapping(value = "/chart/initChart", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object initChart(String uid,String chart_id) {
        Map<String,Object> data = new HashMap<String,Object>();

        String sql = "select id,chart_id,opt_type,opt_sql from waimai_data_agilebi.agilebi_report_chart_option where chart_id="+chart_id;
        List<Map<String,Object>> opts = dbService.listInfos(sql);


        Map<String,Object> list = new HashMap<String,Object>();
        List<Map<String,Object>> options = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> row : opts){
            if(row.get("opt_type").toString().equals("1")){
                list.putAll(row);
            }else{
                options.add(row);
            }
        }

        if(list.size()==0){
            return CommonResult.fail("当前配置没有获取图表信息的配置(opt_type=1 not find)");
        }

        data.put("list",list);
        data.put("option",options);
        return CommonResult.success(data,null);
    }


    /**
     * @param chart_id
     * @param opt_sql
     * @return
     */
    @RequestMapping(value = "/chart/showChart", produces = "application/json;charset=utf8",method = RequestMethod.GET)
    @ResponseBody
    public Object initChart(String chart_id) {
        String sql = "select id,chart_id,opt_type,opt_sql from waimai_data_agilebi.agilebi_report_chart_option where opt_type=1 and chart_id="+chart_id;
        List<Map<String,Object>> opts = dbService.listInfos(sql);
        if(opts!=null && opts.size()>0){
            List<Map<String,Object>> data = dbService.listInfos(opts.get(0).get("opt_sql")+"");
            List<Map<String,String>> titles = reportService.getChartTitle(chart_id);
            return CommonResult.success(data,titles);
        }
        return null;
    }

}

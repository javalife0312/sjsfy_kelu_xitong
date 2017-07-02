package sjs.fy.opt.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jinguowei on 16/10/26.
 */
@Component
public class ReportService {
    @Autowired
    DBService dbService;

    public List<Map<String,String>> getReportTitle(String page_id, String pos_id){
        if(page_id==null){
            page_id = "0";
        }
        if(pos_id==null){
            pos_id = "0";
        }
        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        String sql = "select title_code,title_name from waimai_data_agilebi.agilebi_report_chart_meta where page_id="+page_id+" and pos_id="+pos_id+";";
        List<Map<String,Object>> metas = dbService.listInfos(sql);
        for(Map<String,Object> meta : metas){
            Map<String,String> map = new HashMap<String,String>();
            map.put("data",meta.get("title_code").toString());
            map.put("title",meta.get("title_name").toString());
            titles.add(map);
        }
        return titles;
    }

    /**
     * @param chart_id
     * @return
     */
    public List<Map<String,String>> getChartTitle(String chart_id){
        List<Map<String,String>> titles = new ArrayList<Map<String,String>>();
        String sql = "select opt_title from waimai_data_agilebi.agilebi_report_chart_option where chart_id="+chart_id;
        List<Map<String,Object>> metas = dbService.listInfos(sql);
        if(metas!=null && metas.size()>0){
            String title_str = metas.get(0).get("opt_title")+"";
            String[] rows = title_str.split("\\n");
            for(String row : rows){
                String[] keyval = row.split("=");
                Map<String,String> map = new HashMap<String,String>();
                map.put("data",keyval[0]);
                map.put("title",keyval[1]);
                titles.add(map);
            }
        }
        return titles;
    }
}

package sjs.fy.opt.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constants;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */
@Component
public class FaTingService {
    @Autowired
    private DBService dbService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TimerSerive timerSerive;
    @Autowired
    PropertyService propertyService;


    private String data_base = "sjsfy_opt_shipin";
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    /**
     * 开庭录制
     * @return
     */
    public boolean start_luzhi(Map<String,Object> configs, JLabel starttime_w_label,JTextField anjianbianhao_textfield,JTextField anyou_textfield){
        String room_host = propertyService.getConfig(configs,"diannaozhuji_ip");
        String device_host = propertyService.getConfig(configs,"shexiangji_ip");
        String faguan = propertyService.getConfig(configs,"faguan_name");
        String current = timerSerive.getTimeString();
        String anjianbianhao = anjianbianhao_textfield.getText();
        String anyou = anyou_textfield.getText();

        if(configs.containsKey("sys_luzhi_status")){
            String pk_id = configs.get("sys_luzhi_pk_id").toString();
            String curren = timerSerive.getTimeString();
            String sql = "update "+data_base+".sjsfy_kelu_keluinfo set luxiang_qujian=concat(luxiang_qujian,'"+curren+"','-'),status=3 where id="+pk_id;
            dbService.executeSql(sql);
        }else{
            starttime_w_label.setText(current);
            String sql = "insert into "+data_base+".sjsfy_kelu_keluinfo(room_host,device_host,luxiang_start,luxiang_qujian,status,anjianbianhao,anyou,kaitingshijian,faguan) " +
                    "values('"+room_host+"','"+device_host+"','"+current+"','"+current+"-',"+_Constants.KELU_INFO_STATUS_START_LUZHI+",'"+anjianbianhao+"','"+anyou+"','"+new SimpleDateFormat("YYYY-MM-dd").format(new Date())+"','"+faguan+"')";
            int pk_id = dbService.insertSql(sql,"id");
            configs.put("sys_luzhi_pk_id",pk_id);
        }
        configs.put("sys_luzhi_status", _Constants.KELU_INFO_STATUS_START_LUZHI);
        anjianbianhao_textfield.setEnabled(false);
        anyou_textfield.setEnabled(false);
        return true;
    }

    /**
     * 休庭录制
     * @return
     */
    public boolean xiuting_luzhi(Map<String,Object> configs){
        if(configs.containsKey("sys_luzhi_pk_id")){
            String pk_id = configs.get("sys_luzhi_pk_id").toString();
            String curren = timerSerive.getTimeString();
            String sql = "update "+data_base+".sjsfy_kelu_keluinfo set luxiang_qujian=concat(luxiang_qujian,'"+curren+"',','),status=3 where id="+pk_id;
            dbService.executeSql(sql);
            configs.put("sys_luzhi_status",_Constants.KELU_INFO_STATUS_ZHANTING_LUZHI);
        }
        return true;
    }

    /**
     * 闭庭录制
     * @return
     */
    public boolean stop_xiuting(Map<String,Object> configs,JLabel starttime_w_label,JTextField anjianbianhao_textfield,JTextField anyou_textfield){
        if(configs.containsKey("sys_luzhi_pk_id")){
            String pk_id = configs.get("sys_luzhi_pk_id").toString();
            String sys_luzhi_status = configs.get("sys_luzhi_status").toString();
            String curren = timerSerive.getTimeString();
            if(sys_luzhi_status.equals(_Constants.KELU_INFO_STATUS_START_LUZHI+"")){
                String sql = "update "+data_base+".sjsfy_kelu_keluinfo set luxiang_qujian=concat(luxiang_qujian,'"+curren+"'),status="+_Constants.KELU_INFO_STATUS_WANCHENG_LUZHI+",luxiang_end='"+curren+"' where id="+pk_id;
                dbService.executeSql(sql);
            }else if(sys_luzhi_status.equals(_Constants.KELU_INFO_STATUS_ZHANTING_LUZHI+"")){
                String sql = "update "+data_base+".sjsfy_kelu_keluinfo set status="+_Constants.KELU_INFO_STATUS_WANCHENG_LUZHI+",luxiang_end='"+curren+"' where id="+pk_id;
                dbService.executeSql(sql);
            }
            configs.remove("sys_luzhi_pk_id");
            configs.remove("sys_luzhi_status");
            starttime_w_label.setText("");
            anjianbianhao_textfield.setText("");
            anjianbianhao_textfield.setEnabled(true);
            anyou_textfield.setText("");
            anyou_textfield.setEnabled(true);
        }
        return true;
    }


    /**
     * 预先检测，是否有上次未完结的录制
     * @return
     */
    public Map<String,Object> preCheck(){
        Map<String,Object> result = new HashMap<String,Object>();
        String room_host = deviceService.getLocalAdress();
        String sql = "select * from "+data_base+".sjsfy_kelu_keluinfo where room_host='"+room_host+"' and status in(2,3) order by id desc limit 1";
        List<Map<String,Object>> list = dbService.listInfos(sql);
        if(list!=null && list.size()>0){
            result.putAll(list.get(0));
        }
        return result;
    }

    /**
     * 预先检测，是否有上次未完结的录制,问题处理
     * @return
     */
    public boolean preCheckDeal(Map<String,Object> configs,JButton start_luzhi_btn,JButton zhanting_luzhi_btn,JButton stop_luzhi_btn,JLabel starttime_w_label,JTextField anjianbianhao_textfield,JTextField anyou_textfield){
        Map<String,Object> result = preCheck();
        start_luzhi_btn.setEnabled(false);
        zhanting_luzhi_btn.setEnabled(false);
        stop_luzhi_btn.setEnabled(true);

        configs.put("sys_luzhi_pk_id",result.get("id"));
        configs.put("sys_luzhi_status",result.get("status"));

        starttime_w_label.setText(result.get("luxiang_start").toString());
        anjianbianhao_textfield.setText(result.get("anjianbianhao").toString());
        anjianbianhao_textfield.setEnabled(false);
        anyou_textfield.setText(result.get("anyou").toString());
        anyou_textfield.setEnabled(false);

        return true;
    }
}

package sjs.fy.opt.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/10.
 */
@Component
public class DeviceService {

    @Autowired
    private DBService dbService;

    /**
     * 获取当前主机的IP
     * @return
     */
    public String getLocalAdress(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当前的设备信息
     * @return
     */
    public Map<String,Object> getRoomInfo(){
        Map<String,Object> result = new HashMap<String,Object>();
        String sql = "select fating_desc,faguan_code,faguan_name,shexiangji_ip,shexiangji_username,shexiangji_password,diannaozhuji_ip,diannaozhuji_panfu from sjsfy_opt_shipin.sjsfy_kelu_manageinfo where diannaozhuji_ip='"+getLocalAdress()+"'";
        List<Map<String,Object>> list = dbService.listInfos(sql);
        if(list!=null && list.size()>0){
            result.putAll(list.get(0));
        }
        return result;
    }


    public static void main(String[] args) {
        DeviceService deviceService = new DeviceService();
        System.out.println(deviceService.getLocalAdress());
    }
}

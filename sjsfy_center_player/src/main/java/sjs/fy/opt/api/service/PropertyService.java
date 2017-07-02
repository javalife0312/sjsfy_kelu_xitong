package sjs.fy.opt.api.service;

import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static sjs.fy.opt.api.constant._Constants.LOCAL_PROPERTIES_PATH;

/**
 * Created by Administrator on 2016/11/12.
 */
@Component
public class PropertyService {
    /**
     * 从配置文件中读取信息，防止空指针异常
     * @param key
     * @return
     */
    public String getConfig(Map<String,Object> configs, String key){
        if(configs!=null && configs.containsKey(key)){
            return configs.get(key).toString();
        }else{
            return "";
        }
    }


    public String getValueByKey(String key){
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(LOCAL_PROPERTIES_PATH));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        PropertyService propertyService = new PropertyService();
        String path = propertyService.getValueByKey("iconpath");
        System.out.println(path);
    }
}
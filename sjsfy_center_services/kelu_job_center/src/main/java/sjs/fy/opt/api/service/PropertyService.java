package sjs.fy.opt.api.service;

import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constant;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Created by Administrator on 2016/11/12.
 */
@Component
public class PropertyService {

    public String getValueByKey(String key){
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(_Constant.LOCAL_PROPERTIES_PATH));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
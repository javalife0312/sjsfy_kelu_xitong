package sjs.fy.opt.api.service;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/10.
 */
@Component
public class TimerSerive {

    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    /**
     * 获取当前的时间字符串
     * @return
     */
    public String getTimeString(){
        return dateFormat.format(new Date());
    }

    /**
     * 显示当前时间
     * @param label
     */
    public void showTime(final JLabel label){
        new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        label.setText(dateFormat.format(new Date()));
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.run();
    }
}

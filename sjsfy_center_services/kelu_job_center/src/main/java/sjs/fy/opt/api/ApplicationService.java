package sjs.fy.opt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constant;
import sjs.fy.opt.api.job.service.JobService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/10.
 */
@Component
public class ApplicationService {

    @Autowired
    JobService jobService;

    /**
     * 定时同步IVS设备信息到DB中
     */
    private Runnable sysc_media_job = new Runnable() {
        @Override
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    jobService.startJobs();
                }
            }, _Constant.DELAY_TIME ,  _Constant.PERIOD_TIME);
        }
    };


    /**
     * 程序的入口
     * 1、定时同步ivs数据
     * 2、定时生成job信息
     */
    public void _run(){
        new Thread(sysc_media_job).start();
    }
}

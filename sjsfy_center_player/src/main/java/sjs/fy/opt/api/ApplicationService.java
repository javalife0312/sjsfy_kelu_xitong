package sjs.fy.opt.api;

import sjs.fy.opt.api.service.UploadService;
import sjs.fy.opt.api.view.VlcPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Created by Administrator on 2016/12/4.
 */
@Component
public class ApplicationService {
    @Autowired
    private VlcPlayer vlcPlayer;

    /**
     * 读取本地IP 直接远程登录
     */
    public void run(){
        //自动登录
        if(RuntimeUtil.isMac()){
        }else if(RuntimeUtil.isNix()){
        }else if(RuntimeUtil.isWindows()){
            boolean flag = new NativeDiscovery().discover();
            System.out.println(flag);
            vlcPlayer._run();
        }
    }

}

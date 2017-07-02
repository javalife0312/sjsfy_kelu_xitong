package sjs.fy.opt.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constants;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import java.io.File;
import java.util.Map;


/**
 * Created by Administrator on 2016/11/20.
 */
@Component
public class CameraOptService {
    static {
        new NativeDiscovery().discover();
    }

    @Autowired
    FfmpegService ffmpegService;

    EmbeddedMediaPlayerComponent playerComponent = new EmbeddedMediaPlayerComponent();

    @Autowired
    PropertyService propertyService;
    @Autowired
    DBService dbService;

    /**
     * 获取播放的摄像机URL
     * @param configs
     * @return
     */
    private String getMurl(Map<String,Object> configs){
        String murl = "rtsp://" + configs.get("shexiangji_username") + ":" + configs.get("shexiangji_password") + "@" + configs.get("shexiangji_ip");
        return murl;

    }

    /**
     * 播放
     * @param configs
     * @return
     * 将文件保存到：配置的跟目录的 sjsfy_kelu/案号/文件
     */
    public boolean play(Map<String,Object> configs){
        String murl = getMurl(configs);
        ffmpegService.startSaveViedo(murl,configs);
        getPlayerComponent().getMediaPlayer().playMedia(murl);
        return true;
    }

    /**
     * 停止
     * @return
     */
    public boolean stop(){
        getPlayerComponent().getMediaPlayer().stop();
        ffmpegService.killTask(true);
        return true;
    }

    /**
     * 获取播放器组件
     * @return
     */
    public EmbeddedMediaPlayerComponent getPlayerComponent(){
        return playerComponent;
    }


    public static void main(String[] args) {
        System.out.println("File.separator = "+File.separator);
        System.out.println("File.separatorChar = "+File.separatorChar);
        System.out.println("File.pathSeparator = "+File.pathSeparator);
        System.out.println("File.pathSeparatorChar = "+File.pathSeparatorChar);
    }

}

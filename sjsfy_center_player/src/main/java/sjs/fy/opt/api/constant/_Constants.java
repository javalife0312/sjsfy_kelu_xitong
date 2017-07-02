package sjs.fy.opt.api.constant;

/**
 * Created by Administrator on 2016/11/20.
 */
public interface _Constants {
    //window下的系统配置文件路径

    int VIDEO_STATUS_START_LUZHI=2;
    int VIDEO_STATUS_STOP_LUZHI=3;
    int VIDEO_STATUS_START_KELU_=4;
    int VIDEO_STATUS_STOP_KELU_=1;
    int VIDEO_STATUS_FAIL_KELU_=-1;


    int KELU_INFO_STATUS_START_LUZHI=2;     //播放器点击开始录制
    int KELU_INFO_STATUS_ZHANTING_LUZHI=3;  //播放器点击暂停录制
    int KELU_INFO_STATUS_WANCHENG_LUZHI=4;  //播放器点击完成录制
    int KELU_INFO_STATUS_SHANGCHUAN_LUZHI=5;//系统开始文件上传
    int KELU_INFO_STATUS_START_KELU=6;      //上传文件成功之后开始刻录
    int KELU_INFO_STATUS_DONE=1;            //刻录完成之后，任务完成

    String LOCAL_PATH="sjsfy_kelu";
    String LOCAL_PROPERTIES_PATH="D:/tmp/player.properties";


    int KELU_INFO_CHIXUTIME=5;//单位 分钟



}

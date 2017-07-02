package sjs.fy.opt.api.constant;

/**
 * Created by yangjianzhang on 16/7/20.
 */
public class Constant {
    public static final int KELU_INFO_STATUS_START_LUZHI=2;     //播放器点击开始录制
    public static final int KELU_INFO_STATUS_ZHANTING_LUZHI=3;  //播放器点击暂停录制
    public static final int KELU_INFO_STATUS_WANCHENG_LUZHI=4;  //播放器点击完成录制
    public static final int KELU_INFO_STATUS_SHANGCHUAN_LUZHI=5;//完成上传

    public static final int KELU_INFO_STATUS_XIAZAI_LUZHI=6;    //完成下载,生成了任务
    public static final int KELU_INFO_STATUS_WANCHENG_KELU=1;    //刻录完成
    public static final int KELU_INFO_STATUS_SHIBAI_KELU=-1;    //刻录完成

    public static final int KELU_INFO_STATUS_START_KELU=6;      //上传文件成功之后开始刻录
    public static final int KELU_INFO_STATUS_DONE=1;            //刻录完成之后，任务完成
}

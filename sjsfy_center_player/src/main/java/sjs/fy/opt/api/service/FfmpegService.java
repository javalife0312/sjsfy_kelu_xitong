package sjs.fy.opt.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/10/30.
 */
@Component
public class FfmpegService {
    @Autowired
    PropertyService propertyService;

    private final Map<Process,Map<String,String>> jobs = new ConcurrentHashMap<Process,Map<String,String>>();
    private Timer write;
    private Timer shutdown;


    /**
     * 启动视频写入程序
     * 每隔 5分钟 启动一个启动成
     * 每隔 1分钟 关闭一个写入超过10分钟的窗口
     * @param configs
     * @return
     */
    public boolean startSaveViedo(String murl,final Map<String,Object> configs){
        write = new Timer();
        shutdown = new Timer();
        write.schedule(new TimerTask() {
            @Override
            public void run() {
                String filePath = configs.get("diannaozhuji_panfu") + File.separator + _Constants.LOCAL_PATH + File.separator + configs.get("sys_luzhi_pk_id");
                System.out.println(filePath);
                System.out.println(configs);
                execCaijianCmd(murl,filePath);
            }
        },0,_Constants.KELU_INFO_CHIXUTIME*60*1000);

        shutdown.schedule(new TimerTask() {
            @Override
            public void run() {
                Map<String,Map<String,String>> tasks = getFfmpegTaskInfos();
                killTask(false);
            }
        },0,1*60*1000);
        return true;
    }


    /**
     * @param murl
     * @param filePath
     * @return
     */
    private boolean execCaijianCmd(String murl,String filePath){
        try {
            Runtime runtime = Runtime.getRuntime();
//            String cmd = "ffmpeg -i rtsp://admin:admin123456@192.168.1.64 -vcodec copy -y D:/tmp/"+fileName;
            String filename = filePath + File.separator + System.currentTimeMillis() + ".avi";
            File dir = new File(filePath);
            if(!dir.exists()){
                dir.mkdirs();
            }
            String cmd = "cmd /c start \"勿关！视频录制+\" /min "+propertyService.getValueByKey("local_bat_path")+" "+ murl + " " + filename;
            System.out.println(cmd);
            Process process = runtime.exec(cmd);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    private Map<String,Map<String,String>> getFfmpegTaskInfos(){
        Map<String,Map<String,String>> taskInfos = new HashMap<>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("wmic process where caption=\"cmd.exe\" get commandline,CreationDate,ProcessId");

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null){
                line = line.trim();
                if (line.contains("rtsp")){
                    System.out.println(line);
                    Map<String,String> taskInfo = new HashMap<>();
                    String items[] = line.split("  ");
                    for(String item : items){
                        System.out.println("| : " + item);
                    }
                    taskInfo.put("commandline",items[2]);
                    String createDate = items[3].split("\\.")[0];
                    taskInfo.put("CreationDate",createDate);
                    taskInfo.put("ProcessId",items[4]);
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date now = new Date();
                    try{
                        taskInfo.put("ChiXuTime",(now.getTime() - dateFormat.parse(createDate).getTime())+"");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    taskInfo.put("NowDate",dateFormat.format(now));
                    taskInfos.put(items[4],taskInfo);
                }
            }
            return taskInfos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskInfos;
    }

    /**
     * @param killAll
     * @return
     */
    public boolean killTask(boolean killAll){
        Map<String,Map<String,String>> tasks = getFfmpegTaskInfos();
        try{
            for(String processId : tasks.keySet()){
                Map<String,String> task = tasks.get(processId);
                boolean killFlag = false;
                if(killAll){
                    killFlag = true;
                    if(write!=null){
                        write.cancel();;
                        write = null;
                    }
                    if(shutdown!=null){
                        shutdown.cancel();
                        shutdown = null;
                    }
                }else {
                    if(Long.valueOf(task.get("ChiXuTime"))/1000/60>_Constants.KELU_INFO_CHIXUTIME){ //单位分钟
                        killFlag = true;
                    }
                }
                if(killFlag){
                    Runtime runtime = Runtime.getRuntime();
                    String cmd = "taskkill /pid "+processId+" -t -f";
                    System.out.println(cmd);
                    Process process = runtime.exec(cmd);
                    System.out.println("kill : " + process.waitFor());
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args) throws Exception{
    }
}

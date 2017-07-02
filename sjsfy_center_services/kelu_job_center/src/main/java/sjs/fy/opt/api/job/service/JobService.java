package sjs.fy.opt.api.job.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.ini4j.Ini;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.common.DBService;
import sjs.fy.opt.api.common.FileUtils;
import sjs.fy.opt.api.service.ExcelService;
import sjs.fy.opt.api.service.IniService;
import sjs.fy.opt.api.constant._Constant;
import sjs.fy.opt.api.service.PropertyService;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/2/27.
 */
@Component
public class JobService {
    private String PTBurnJobServicesDir = "C:\\PTBurnJobs\\";
    private final Map<String,Object> QUANJU_JOBS = new HashMap<>();

    @Autowired
    DBService dbService;
    @Autowired
    IniService iniService;
    @Autowired
    ExcelService excelService;
    @Autowired
    PropertyService propertyService;


    /**
     * 整个刻录服务的入口程序
     * 1、读取需要刻录的任务信息
     * 2、将刻录任务放入download队列进行下载
     * 3、生成job文件
     * 4、将job文件同步到刻录机器进行刻录
     */
    public void startJobs(){
        Runnable preJobs = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        SystemStatus systemStatus = systemServiceCheck();
                        if(systemStatus!=null && systemStatus.getSysErrorNumber().equals("0")){
                            DeviceStatus deviceStatus = deviceServiceCheck(systemStatus.getStatusFile());
                            if(deviceStatus!=null && "0".equals(deviceStatus.getSysErrorNumber())){
                                preJob();
                                if(QUANJU_JOBS.size()>0 && QUANJU_JOBS.get("job_status").toString().equals("sql_get")) {
                                    boolean flag = false;
                                    Integer id = null;
                                    try {
                                        id = Integer.valueOf(QUANJU_JOBS.get("id").toString());
                                        flag = download2(id);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (flag) {
                                        flag = createJob(QUANJU_JOBS);
                                        if (flag) {
                                            dbService.executeSql("update sjsfy_opt_shipin.sjsfy_kelu_keluinfo set status=" + _Constant.KELU_INFO_STATUS_XIAZAI_LUZHI + " where id=" + id);
                                            try {
                                                KeluJobStatus keluJobStatus = getKeluJobStatus(id+"");
                                                if(keluJobStatus.getJobState().equals("2")){ //刻录完成 删除目录
                                                    dbService.executeSql("update sjsfy_opt_shipin.sjsfy_kelu_keluinfo set status=" + _Constant.KELU_INFO_STATUS_WANCHENG_KELU + " where id=" + id);
                                                    String dowload_local_path = propertyService.getValueByKey("LOCAL_PATH_KELU") + "/" + QUANJU_JOBS.get("id");
                                                    FileUtils.deleteDir(new File(dowload_local_path));
                                                    QUANJU_JOBS.clear();
                                                }else {
                                                    dbService.executeSql("update sjsfy_opt_shipin.sjsfy_kelu_keluinfo set status=" + _Constant.KELU_INFO_STATUS_SHIBAI_KELU + " where id=" + id);
                                                    String dowload_local_path = propertyService.getValueByKey("LOCAL_PATH_KELU") + "/" + QUANJU_JOBS.get("id");
                                                    FileUtils.deleteDir(new File(dowload_local_path));
                                                    QUANJU_JOBS.clear();
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                }
                            }else {
                                System.out.println("刻录设备，目前处于故障状态，请管理员处理");
                            }
                        }else{
                            System.out.println("刻录系统，目前处于故障状态，请管理员处理");
                        }
                        Thread.sleep(10*1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };

//        new Thread(systemCheck).start();
        new Thread(preJobs).start();

    }


    /**
     * 检测系统设备状态当前如何
     * @return
     * @throws Exception
     */
    private SystemStatus systemServiceCheck() throws Exception {
        String filepath = PTBurnJobServicesDir + "Status\\SystemStatus.txt";
        Wini wini = iniService.getWini(filepath);
        String sysErrorString = wini.get("System","SysErrorString");
        String sysErrorNumber = wini.get("System","SysErrorNumber");
        String statusFile = wini.get("System","StatusFile");
        return new SystemStatus(sysErrorString,sysErrorNumber,statusFile);
    }


    /**
     * 实时监控 读取系统状态和设备硬件信息
     * @param keluStatusFile
     * @return
     * @throws Exception
     */
    private DeviceStatus deviceServiceCheck(String keluStatusFile) throws Exception{
        String filepath = PTBurnJobServicesDir + "Status\\" + keluStatusFile;
        Wini wini = iniService.getWini(filepath);
        String sysErrorString = wini.get("System","SysErrorString");
        String sysErrorNumber = wini.get("System","SysErrorNumber");
        String systemStatus = wini.get("System","SystemStatus");
        return new DeviceStatus(sysErrorString,sysErrorNumber,systemStatus);
    }

    /**
     * 获取正在刻录的任务的状态
     * @param id
     * @return
     * @throws Exception
     */
    private KeluJobStatus getKeluJobStatus(String id) throws Exception{
        String filepath = PTBurnJobServicesDir + "Status\\Disc Publisher 4100 Series.txt";
        Wini ini = iniService.getWini(filepath);
//        System.out.println(ini.get("JobList").isEmpty());
//        if(ini.get("JobList")!=null){ //说明有任务正在刻录中
//            if(ini.get("JobList").containsValue(id)){ //说明当前任务正在刻录中
//
//            }
//        }
        System.out.println(ini.get(id+""));
        //从job info 中提取任务状态。如果找不到则每次sleep 10秒。直到找到为止，如果找不到责任务失败。重新来一遍
        boolean stopFlag = false;
        int tryTimes = 0;
        while (ini.get(id)==null && !stopFlag){
            Thread.sleep(10*1000);
            tryTimes++;
            if(tryTimes>6){
                stopFlag = true;
            }
        }
        if(ini.get(id)==null){
            return new KeluJobStatus(Integer.valueOf(id),"3","-1","任务找不到");
        }else {
            stopFlag = false;
            tryTimes = 0;
            KeluJobStatus keluJobStatus = new KeluJobStatus(Integer.valueOf(id),"3","","");
            while (!stopFlag){
                String JobState = ini.get(id,"JobState");
                if(JobState.equals("2") || JobState.equals("3")){
                    keluJobStatus.setJobState(JobState);
                    if(ini.get(id,"JobErrorNumber")!=null){
                        String JobErrorNumber = ini.get(id,"JobErrorNumber");
                        keluJobStatus.setJobErrorNumber(JobErrorNumber);
                    }
                    if(ini.get(id,"JobErrorString")!=null){
                        String JobErrorString = ini.get(id,"JobErrorString");
                        keluJobStatus.setJobErrorString(JobErrorString);
                    }
                    stopFlag = true;
                }else {
                    Thread.sleep(10*1000);
                    tryTimes++;
                    if(tryTimes>60){
                        keluJobStatus.setJobState("3");
                        keluJobStatus.setJobErrorNumber("");
                        keluJobStatus.setJobErrorString("刻录超过10分钟。。。。");
                        stopFlag = true;
                    }
                }
            }
            return keluJobStatus;
        }
    }

    public static void main(String[] args) throws Exception{
        JobService jobService = new JobService();
        jobService.getKeluJobStatus(26+"");
    }

    /**
     * 从刻录信息表中提取需要刻录的信息
     * 如果任务列表大于 JOB_QUENE_SIZE 则等待下一轮的排队
     */
    private void preJob(){
        String sql = "select id,device_host,anjianbianhao,anyou,kaitingshijian,faguan,`status` from sjsfy_opt_shipin.sjsfy_kelu_keluinfo where `status`="+_Constant.KELU_INFO_STATUS_SHANGCHUAN_LUZHI+" order by id limit 1";
        List<Map<String,Object>> jobs = dbService.listInfos(sql);
        for(Map<String,Object> job : jobs){
            if(QUANJU_JOBS.size() == 0){
                job.put("job_status","sql_get");
                QUANJU_JOBS.putAll(job);
            }
        }
    }

    /**
     * 从队列中获取需要下载的文件
     */
    private boolean download2(Integer id) throws IOException {
        if(QUANJU_JOBS.get("status").toString().equals(_Constant.KELU_INFO_STATUS_SHANGCHUAN_LUZHI+"")){
            String download_hdfs_path = propertyService.getValueByKey("HDSF_MASTER") + "/sjsfy/" + QUANJU_JOBS.get("device_host") + "/" + QUANJU_JOBS.get("id");
            String dowload_local_path = propertyService.getValueByKey("LOCAL_PATH_KELU") + "/" + QUANJU_JOBS.get("id") + "/";

            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS", propertyService.getValueByKey("HDSF_MASTER"));
            FileSystem fileSystem = FileSystem.get(configuration);

            InputStream in = null;
            OutputStream outputStream = null;

            FileStatus[] fileStatuses = fileSystem.listStatus(new Path(download_hdfs_path));
            for(FileStatus fileStatus : fileStatuses){
                try{
                    long file_big_mb = fileStatus.getLen()/1000/1000;

                    System.out.println(fileStatus.getPath()  + " : " + file_big_mb);


                    in = fileSystem.open(fileStatus.getPath());
                    File outdir = new File(dowload_local_path);
                    if(!outdir.exists()){
                        outdir.mkdirs();
                    }
                    outputStream = new FileOutputStream(dowload_local_path + fileStatus.getPath().getName());
                    System.out.println(fileStatus.getPath());
                    IOUtils.copyBytes(in,outputStream,4096,false);
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }finally {
                    IOUtils.closeStream(in);
                    IOUtils.closeStream(outputStream);
                }

            }
        }
        return true;
    }

    /**
     * 生成刻录的Job文件
     * @param job
     * @throws Exception
     */
    private boolean createJob(Map<String, Object> job){
        BufferedWriter bufferedWriter = null;
        try{
            String[] header = {"anhao","anyou","kaitingshijian","jingbanren"};
            System.out.println("createJob : " + job);
            String[] data = {job.get("anjianbianhao").toString(),job.get("anyou").toString(),job.get("kaitingshijian").toString(),job.get("faguan").toString()};
            excelService.createJobMergeFile(job.get("id").toString(),header,data);

            File job_file = new File(PTBurnJobServicesDir + job.get("id") + ".JRQ");
            if(job_file.exists()){
                job_file.delete();
            }
            job_file = new File(PTBurnJobServicesDir + job.get("id") + ".JRQ");
            job_file.createNewFile();

            bufferedWriter = new BufferedWriter(new FileWriter(job_file));
            bufferedWriter.write("JobID="+job.get("id")+"\n");

            String dowload_local_path = propertyService.getValueByKey("LOCAL_PATH_KELU") + "/" + job.get("id")+"_"+job.get("anjianbianhao");
            bufferedWriter.write("Data="+dowload_local_path+"\n");
            bufferedWriter.write("PrintLabel="+propertyService.getValueByKey("LOCAL_PATH_KELU_LABEL")+"/"+_Constant.STD_MERAGE_FILE+"\n");
            bufferedWriter.write("VolumeName="+job.get("id")+"\n");
            bufferedWriter.write("BurnSpeed="+8+"\n");

            bufferedWriter.flush();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally{
            if(bufferedWriter!=null){
                try{
                    bufferedWriter.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}

/**
 * PTBurnJobs - SystemStatus服务
 */
class SystemStatus implements Serializable{
    private String sysErrorString;
    private String sysErrorNumber;
    private String statusFile;

    public SystemStatus(String sysErrorString, String sysErrorNumber,String statusFile) {
        this.sysErrorString = sysErrorString;
        this.sysErrorNumber = sysErrorNumber;
        this.statusFile = statusFile;
    }

    public String getSysErrorString() {
        return sysErrorString;
    }

    public String getSysErrorNumber() {
        return sysErrorNumber;
    }

    public String getStatusFile() {
        return statusFile;
    }
}

/**
 * PTBurnJobs - DeviceStatus 服务
 */
class DeviceStatus implements Serializable{
    private String sysErrorString;
    private String sysErrorNumber;
    private String systemStatus;

    public DeviceStatus(String sysErrorString, String sysErrorNumber,String systemStatus) {
        this.sysErrorString = sysErrorString;
        this.sysErrorNumber = sysErrorNumber;
        this.systemStatus = systemStatus;
    }

    public String getSysErrorString() {
        return sysErrorString;
    }

    public String getSysErrorNumber() {
        return sysErrorNumber;
    }

    public String getSystemStatus() {
        return systemStatus;
    }
}

/**
 * PTBurnJobs - 刻录任务的实体
 */
class KeluJobStatus implements Serializable{
    private int id;
    private String jobState;
    private String jobErrorNumber;
    private String jobErrorString;
    public KeluJobStatus(int id, String jobState, String jobErrorNumber, String jobErrorString) {
        this.id = id;
        this.jobState = jobState;
        this.jobErrorNumber = jobErrorNumber;
        this.jobErrorString = jobErrorString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public String getJobErrorNumber() {
        return jobErrorNumber;
    }

    public void setJobErrorNumber(String jobErrorNumber) {
        this.jobErrorNumber = jobErrorNumber;
    }

    public String getJobErrorString() {
        return jobErrorString;
    }

    public void setJobErrorString(String jobErrorString) {
        this.jobErrorString = jobErrorString;
    }
}

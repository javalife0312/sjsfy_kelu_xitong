package sjs.fy.opt.api.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constants;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class UploadService {
    private Map<String,Map<String,Object>> cache = new ConcurrentHashMap<>();
    private Map<String,Object> configs = new ConcurrentHashMap<>();

    @Autowired
    DBService dbService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    PropertyService propertyService;

    private String HDSF_MASTER;

    @PostConstruct
    public void  init(){
        HDSF_MASTER = propertyService.getValueByKey("hdfs_master");
        configs.putAll(deviceService.getRoomInfo());

        //hdfs
        Runnable hdfs = new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        try {
                            String sql = "select id,anjianbianhao,device_host,kelu_dir,`status` from sjsfy_opt_shipin.sjsfy_kelu_keluinfo where room_host='"+configs.get("diannaozhuji_ip")+"' and `status`=" + _Constants.KELU_INFO_STATUS_WANCHENG_LUZHI;
                            List<Map<String,Object>> infos =  dbService.listInfos(sql);
                            for(Map<String,Object> info : infos){
                                cache.put(info.get("id").toString(),info);
                            }
                            for(String id : cache.keySet()){

                                String panfu = configs.get("diannaozhuji_panfu").toString();
                                String root_dir = _Constants.LOCAL_PATH;
                                String filepath = panfu + File.separator + root_dir;
                                filepath = filepath + File.separator + id + File.separator;

                                boolean success = copyFiles(filepath,cache.get(id).get("device_host").toString());
                                if(success){
                                    if(success){
                                        success = updateStatus(id);
                                        if(success){
                                            cache.remove(id);
//                                            deleteDir(filepath);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("文件上传系统故障，目前处于故障状态，请管理员处理");
                            e.printStackTrace();
                        }
                    }
                }, 1000*3 , 1000*60*5);
            }
        };
        new Thread(hdfs).start();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
		Configuration configuration = new Configuration();
		configuration.set("fs.defaultFS", "hdfs://193.1.51.43:9000");
		FileSystem fileSystem = FileSystem.get(configuration);

		Path path = new Path("hdfs://193.1.51.43:9000/template");

		Path local = new Path("D:\\soft-work\\work\\git\\sjsfy_center_services\\kelu_job_center\\doc\\template.csv");
		Path hdfs = new Path("/");

		fileSystem.copyFromLocalFile(false,local,hdfs);

        FileStatus[] fileStatuses = fileSystem.listStatus(hdfs);
        for(FileStatus fileStatus : fileStatuses){
            System.out.println(fileStatus.getPath());
        }
    }


    /**
     * 将本地目录下的文件上传到Hdfs路径
     * @param dir
     * @return
     */
    public boolean copyFiles(String dir,String ip){
        try {
            Configuration configuration = new Configuration();
            configuration.set("fs.defaultFS", HDSF_MASTER);
            File fileDir = new File(dir);
            //约定目录仅有一层
            if(fileDir.isDirectory()){
                File[] localFiles = fileDir.listFiles();
                for(File localFile : localFiles){
                    FileSystem fileSystem = FileSystem.get(configuration);
                    if(!fileSystem.exists(new Path(local2hdfs_dir(ip,fileDir.getName()).toString()))){
                        fileSystem.mkdirs(new Path(local2hdfs_dir(ip,fileDir.getName())));
                    }
                    System.out.println(localFile.getAbsoluteFile());
                    fileSystem.copyFromLocalFile(false,new Path(localFile.getAbsolutePath()),new Path(local2hdfs_dir(ip,fileDir.getName())));
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

	/**
	 * 将文件上传到HDFS之后删除本地文件
	 * @param dir
	 * @return
	 */
	private boolean deleteDir(String dir) throws Exception{
		File fileDir = new File(dir);
		//约定目录仅有一层
		if(fileDir.isDirectory()){
			File[] localFiles = fileDir.listFiles();
			for(File localFile : localFiles){
				localFile.delete();
			}
			fileDir.delete();
		}
		return true;
	}


    /**
     * 上传完成之后更新状态
     * @param id
     * @return
     */
    private boolean updateStatus(String id) throws Exception{
        String sql = "update sjsfy_opt_shipin.sjsfy_kelu_keluinfo set `status`="+ _Constants.KELU_INFO_STATUS_SHANGCHUAN_LUZHI +" where id="+id;
        dbService.executeSql(sql);
        return true;
    }


//	/**
//	 * 将本地文件转换成hdfs文件
//	 * @param fileName
//	 * @param ip
//	 * @param dir
//	 * @return
//	 */
//	private String local2hdfs(String fileName,String ip,String dir){
//		StringBuffer result = new StringBuffer(HDSF_MASTER).append("/");
//		result.append(ip).append("/");
//		result.append(dir).append("/");
//		result.append(fileName);
//		return result.toString();
//	}

    /**
     * 将本地文件转换成hdfs文件
     * @param ip
     * @param dir
     * @return
     */
    private String local2hdfs_dir(String ip,String dir){
        StringBuffer result = new StringBuffer();
        result.append(HDSF_MASTER).append("/sjsfy/");
        result.append(ip).append("/");
        result.append(dir);
        return result.toString();
    }
}

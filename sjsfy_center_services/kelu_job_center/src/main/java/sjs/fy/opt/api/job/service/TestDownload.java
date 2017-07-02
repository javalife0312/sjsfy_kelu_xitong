package sjs.fy.opt.api.job.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jinguowei on 17/5/24.
 */
public class TestDownload {
    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://172.18.128.110:9000");
        FileSystem fileSystem = FileSystem.get(configuration);
        InputStream in = null;
        OutputStream outputStream = null;

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/study"));
        for(FileStatus fileStatus : fileStatuses){
            System.out.println(fileStatus.getPath().getName());
            try{
                in = fileSystem.open(fileStatus.getPath());
                outputStream = new FileOutputStream("/Users/jinguowei/test/" + fileStatus.getPath().getName());
                IOUtils.copyBytes(in,outputStream,4096,false);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                IOUtils.closeStream(in);
                IOUtils.closeStream(outputStream);
            }

        }


//        try {
//            in = fileSystem.open(new Path("hdfs://172.18.128.110:9000/study/in_use.lock"));
//            IOUtils.copyBytes(in, outputStream, 4096, false);
//        }finally {
//            IOUtils.closeStream(in);
//        }
    }
}

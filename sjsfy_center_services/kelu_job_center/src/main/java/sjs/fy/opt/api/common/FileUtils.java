package sjs.fy.opt.api.common;

import java.io.File;

/**
 * Created by JGW on 2017/6/5.
 */
public class FileUtils {

    /**
     * 删除一层目录
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static void main(String[] args) {
        String dir = "D:\\sjsfy_kelu_jobs\\137";
        FileUtils.deleteDir(new File(dir));
    }
}

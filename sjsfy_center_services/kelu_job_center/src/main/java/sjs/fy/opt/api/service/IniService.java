package sjs.fy.opt.api.service;

import org.ini4j.Wini;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by JGW on 2017/5/7.
 */
@Component
public class IniService {
    /**
     * 获取对象
     * @param filename
     * @return
     * @throws Exception
     */
    public Wini getWini(String filename) throws Exception{
        Wini ini = new Wini(new File(filename));
        return ini;
    }

    public static void main(String[] args) throws Exception{
        Wini ini = new Wini(new File("C:\\PTBurnJobs\\Status\\SystemStatus.txt"));
        String SysErrorString = ini.get("System", "SysErrorString");
        String SysErrorNumber = ini.get("System", "SysErrorNumber");
        String SysErrorNumber1 = ini.get("System", "SysErrorNumber1");
        System.out.println(true);
    }
}

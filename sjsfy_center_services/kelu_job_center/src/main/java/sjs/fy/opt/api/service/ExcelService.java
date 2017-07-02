package sjs.fy.opt.api.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sjs.fy.opt.api.constant._Constant;

import java.io.*;
import java.util.Map;

/**
 * Created by jinguowei on 17/5/24.
 */
@Component
public class ExcelService {

    @Autowired
    PropertyService propertyService;

//    /**
//     * 生成盘面的Label的EXCEL
//     * @param id
//     * @param rowHeader
//     * @param rowData
//     * @return
//     */
//    public boolean createJobExcel(String id, String[] rowHeader, String[] rowData){
//        try{
//            File file = new File(_Constant.LOCAL_PATH_KELU_LABEL+"\\" + id + ".xls");
//            if(file.exists()){
//                file.delete();
//            }
//            Workbook wb = new HSSFWorkbook();
//            Sheet sheet = wb.createSheet();
//
//            Row header = sheet.createRow((short)0);
//            for(int i=0;i<rowHeader.length;i++){
//                header.createCell(i).setCellValue(rowHeader[i]);
//            }
//            Row data = sheet.createRow((short)1);
//            for(int i=0;i<rowHeader.length;i++){
//                data.createCell(i).setCellValue(rowData[i]);
//            }
//            FileOutputStream fileOut = new FileOutputStream(file);
//            wb.write(fileOut);
//            fileOut.close();
//        }catch (Exception e){
//
//        } finally {
//
//        }
//        return true;
//    }

    /**
     * 生成盘面的Label的EXCEL
     * @param id
     * @param rowHeader
     * @param rowData
     * @return
     */
    public boolean createJobMergeFile(String id, String[] rowHeader, String[] rowData){
        BufferedWriter writer = null;
        try{
            File file = new File(propertyService.getValueByKey("LOCAL_PATH_KELU_LABEL")+"/" + _Constant.STD_MERAGE_FILE);
            if(file.exists()){
                file.delete();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            StringBuilder headBuilder = new StringBuilder();
            StringBuilder dataBuilder = new StringBuilder();
            for(int i=0;i<rowHeader.length;i++){
                headBuilder.append(rowHeader[i]);
                if(i == rowHeader.length-1){
                    headBuilder.append("\n");
                }else {
                    headBuilder.append(",");
                }
            }
            for(int i=0;i<rowData.length;i++){
                dataBuilder.append(rowData[i]);
                if(i == rowData.length-1){
                    dataBuilder.append("\n");
                }else {
                    dataBuilder.append(",");
                }
            }
            writer.write(headBuilder.toString());
            writer.write(dataBuilder.toString());
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(writer!=null){
                try{
                    writer.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return true;
    }

//    /**
//     * 生成盘面的Label的EXCEL
//     * @param id
//     * @return
//     */
//    public boolean createJobLabel(String id){
//
//        try{
//            File src = new File(_Constant.LOCAL_PATH_KELU_LABEL + "\\template.std");
//            File desc = new File(_Constant.LOCAL_PATH_KELU_LABEL + "\\"+id +".std");
//            IOUtils.copy(new FileInputStream(src),new FileOutputStream(desc));
//        }catch (Exception e){
//
//        } finally {
//
//        }
//        return true;
//    }
}

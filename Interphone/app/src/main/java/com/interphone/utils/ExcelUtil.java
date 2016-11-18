package com.interphone.utils;

import com.interphone.bean.PowerTestFileValue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by zhuj on 2016/11/18 17:39.
 */
public class ExcelUtil {

  private static volatile ExcelUtil instance = null;

  private ExcelUtil() {}

  public static ExcelUtil getInstance() {
    if (instance == null) {
      synchronized (ExcelUtil.class) {
        if (instance == null ) {
          instance = new ExcelUtil();
        }
      }
    }
    return instance;
  }


  public boolean saveExcel(String name,boolean isCheck,  List<String> list) {
    File file = FileUtil.getSaveFile(name);
    OutputStream os;
    try {
      os = new FileOutputStream(file);
      createExcel(os, isCheck, list);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void createExcel(OutputStream os, boolean isCheck,List<String> list) throws WriteException,IOException {
    //创建工作薄
    WritableWorkbook workbook = Workbook.createWorkbook(os);
    //创建新的一页
    WritableSheet sheet = workbook.createSheet("First Sheet",0);
    Label check = new Label(0,0,isCheck ?"true": "false");
    sheet.addCell(check);
    int i=1;
    for (String s : list) {
      Label qinghua = new Label(0,i,s);
      sheet.addCell(qinghua);
      i++;
    }

    //把创建的内容写入到输出流中，并关闭输出流
    workbook.write();
    workbook.close();
    os.close();
  }

  public List<String> readExcelList() {
    List<String> list = new ArrayList<>();
    File file = FileUtil.getSavePath();
    File[] files = file.listFiles();
    for (File f : files) {
      if (!f.isDirectory()) {
        if (f.getName().endsWith("xls")) {
          list.add(f.getAbsoluteFile().getAbsolutePath());
        }
      }
    }
    return list;
  }

  public PowerTestFileValue parseExcel(String path) {
    PowerTestFileValue ptf ;
    try {
      Workbook wb;
      ptf = new PowerTestFileValue();
      File file = new File(path);
        wb = Workbook.getWorkbook(file);
        Sheet sheet = wb.getSheet(0);
        int row = sheet.getRows();
        int col = sheet.getColumns();
      String checkStr = sheet.getCell(0, 0).getContents();
        ptf.setCheck(checkStr.equals("true"));
        List<Integer> list =new ArrayList<>();
        for (int i=1; i<row; i++) {
          list.add(Integer.parseInt(sheet.getCell(0, i).getContents()));
        }
        ptf.setMList(list);
        return ptf;
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    return null;
  }
}

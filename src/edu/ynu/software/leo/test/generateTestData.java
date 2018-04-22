package edu.ynu.software.leo.test;

import org.apache.poi.ss.usermodel.*;

import java.io.*;

public class generateTestData {
    public static void main(String[] args) {
        read();
    }

    public static void read() {
        File file = new File("data/test/NSGA-II_Test1_picture.xlsx");
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(1);
            //总行数
            int rowLength = sheet.getLastRowNum()+1;
            //工作表的列
            Row row = sheet.getRow(0);
            //总列数
            int colLength = row.getLastCellNum();
            //得到指定的单元格
            Cell cell = row.getCell(0);
            //得到单元格样式
//            CellStyle cellStyle = cell.getCellStyle();
            System.out.println("行数：" + rowLength + ",列数：" + colLength);
            for (int i = 0; i < rowLength; i++) {
                System.out.println("i:"+i);
                row = sheet.getRow(i);
                for (int j = 0; j < colLength; j++) {
                    System.out.println("j:"+j);
                    if (row.getCell(j) != null) {
                        cell = row.getCell(j);
                        CellStyle cs = cell.getCellStyle();
                        Color color = cs.getFillBackgroundColorColor();
                        System.out.print(i+","+j+":"+color+"\t");
                    }
                    //Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串时就有可能报异常：
                    //Cannot get a STRING value from a NUMERIC cell
                    //将所有的需要读的Cell表格设置为String格式
                }
                System.out.println();
            }

            //将修改好的数据保存
//            OutputStream out = new FileOutputStream(file);
//            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package edu.ynu.software.leo.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by maxleo on 17-11-30.
 */
public class Test {
    public static void main(String[] args) {
        Double number = -15.0;
        System.out.println(-number);
        String filePath = "data/output/result1.data";
        WriteStringToFile3(filePath);
    }

    public static void WriteStringToFile3(String filePath) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filePath));
            pw.println("abc ");
            pw.println("def ");
            pw.println("hef ");
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

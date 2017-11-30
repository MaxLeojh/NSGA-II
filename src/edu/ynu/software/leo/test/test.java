package edu.ynu.software.leo.test;

import edu.ynu.software.leo.algorithm.Iris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by maxleo on 17-11-2.
 */
public class test {
    public static List<Iris> irisData;
    public static void main(String[] args) {
        String filePath = "/home/maxleo/Data/Iris set/Iris.data";
        irisData = readFileByLines(filePath);
        for (int i = 0; i < irisData.size(); i++) {
            System.out.println("["+i+"]"+irisData.get(i).sepalL+","+irisData.get(i).sepalW+","+irisData.get(i).petalL+","+irisData.get(i).petalW+","+irisData.get(i).type);
        }

    }

    public static List<Iris> readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        List<Iris> irisData = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                String[] temp = tempString.split(",");
                Iris iris = new Iris();
                iris.sepalL = Double.parseDouble(temp[0]);
                iris.sepalW = Double.parseDouble(temp[1]);
                iris.petalL = Double.parseDouble(temp[2]);
                iris.petalW = Double.parseDouble(temp[3]);
                iris.type = temp[4];
                irisData.add(iris);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();

                }
            }
        }
        return irisData;
    }

}

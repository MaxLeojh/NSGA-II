package edu.ynu.software.leo.test;
import edu.ynu.software.leo.algorithm.NSGA_II;
import edu.ynu.software.leo.algorithm.Population;
import edu.ynu.software.leo.dataSet.Iris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxleo on 17-11-2.
 */
public class Main {
    public static List<Iris> dataSet;
    public static void main(String[] args) {
        String filePath = "data/Iris set/Iris.data";
        dataSet = readIrisData(filePath);

          //check file input
//        for (int i = 0; i < dataSet.size(); i++) {
//            System.out.println("["+i+"]"+dataSet.get(i).sepalL+","+dataSet.get(i).sepalW+","+dataSet.get(i).petalL+","+dataSet.get(i).petalW+","+dataSet.get(i).type);
//        }

        Population population = new Population(true);
        NSGA_II nsga_ii = new NSGA_II();

        for (int i = 0; i < 100; i++) {
            population = nsga_ii.evolution(population);
            System.out.println("-------"+i+"----------");
        }



    }

    public static List<Iris> readIrisData(String fileName) {
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

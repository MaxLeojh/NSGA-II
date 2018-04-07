package edu.ynu.software.leo.test;
import edu.ynu.software.leo.algorithm.Individual;
import edu.ynu.software.leo.algorithm.NSGA_II;
import edu.ynu.software.leo.algorithm.Population;
import edu.ynu.software.leo.dataSet.Iris;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxleo on 17-11-2.
 */
public class Main {
    public static List<Iris> dataSet;
    public static void main(String[] args) {
        Integer iterationNum = 150;
        String filePath = "data/Iris set/Iris.data";
//        String filePath = "data/Test1.data";
        String outFilePath = "data/output/result.data";

        dataSet = readIrisData(filePath);
        System.out.println("Read data, successful.");

          //check file input
//        for (int i = 0; i < dataSet.size(); i++) {
//            System.out.println("["+i+"]"+dataSet.get(i).sepalL+","+dataSet.get(i).sepalW+","+dataSet.get(i).petalL+","+dataSet.get(i).petalW+","+dataSet.get(i).type);
//        }

        Population population = new Population(true);
        NSGA_II nsga_ii = new NSGA_II();
        System.out.println("Initialize, successful");

        for (int i = 0; i < iterationNum; i++) {
            population = nsga_ii.evolution(population);
            System.out.println("Iteration "+i+" : Done.");
            System.out.println();
        }
        System.out.println("Evolution complete!");

        fileOutput(population,outFilePath,iterationNum);
        System.out.println("Output complete!");


//        Individual individual = population.individualList.get(0);
//        System.out.println("genes are as follows:");
//        for (int i = 0; i < individual.gene.size(); i++) {
//            System.out.println(i+":"+individual.gene.get(i));
//        }
//
//        System.out.println("DB:"+individual.adaptiveValues.get(0));
//        System.out.println("DI:"+individual.adaptiveValues.get(1));
//        System.out.println("SC:"+individual.adaptiveValues.get(2));

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

    public static void fileOutput(Population population, String filePath, Integer iterationNum) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filePath));
            pw.println("Population size: "+population.size()+"; Iteration times:"+iterationNum+";");
            pw.println();
            for (int i = 0; i < population.size(); i++) {
                pw.println("-----------solution "+i+"-----------");
                Individual individual = population.individualList.get(i);
                pw.println("Rank:"+individual.rank+"; Cluster number:"+individual.clusterCount+"; Adapt values:"+ -individual.adaptiveValues.get(0)+" "+individual.adaptiveValues.get(1)+" "+individual.adaptiveValues.get(2));
                for (int j = 0; j < individual.gene.size(); j++) {
                    pw.println(j+"\t"+individual.gene.get(j));
                }
            }
            pw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

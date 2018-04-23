package edu.ynu.software.leo.test;

import edu.ynu.software.leo.algorithm.CombineAndArrangement;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by maxleo on 17-11-30.
 */
public class Test {
    public static void main(String[] args) {
//        patten(50);
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

//    public static ArrayList<ArrayList<Integer>> patten(Integer clusterSize) {
//        if (clusterSize < 3) return null;
//        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
//        ArrayList<Integer> temp = new ArrayList<>();
//        temp.add(0);
//        temp.add(1);
//        result.add(temp);
//        Integer size = clusterSize-2;
//        ArrayList<Integer> array = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            array.add(2+i);
//        }
//        Integer [] com = (Integer []) (array.toArray(new Integer[size]));
//        CombineAndArrangement caa = new CombineAndArrangement();
//        caa.arrangement(size,com);
//        for (int i = 0; i < caa.result.size(); i++) {
//            ArrayList<Integer> tmp = new ArrayList<>();
//            tmp.add(0);
//            tmp.addAll(caa.result.get(i));
//            tmp.add(1);
//            result.add(tmp);
//        }
//        return result;
//    }

}

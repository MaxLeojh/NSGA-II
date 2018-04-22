package edu.ynu.software.leo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hdwang on 2017-10-25.
 * 组合、排列、可重复排列
 */
public class CombineAndArrangement {

    private ArrayList<Integer> tmpArr = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> result = new ArrayList<>();
//    public static void main(String[] args) {
//        int [] com = {1,2,3};
//        int k = 3;
//        if(k > com.length || com.length <= 0){
//            return ;
//        }
//        System.out.println("\n排列结果：");
//        arrangement(k,com);
//        for (int i = 0; i < out.size(); i++) {
//            System.out.println(out.get(i).toString());
//        }
//    }

    /**
     * 排列
     * 按照无序（随机）的方式取出元素，就是排列,元素个数[A arr.len 3]
     * @param k 选取的元素个数
     * @param arr 数组
     */
    public void arrangement(int k,Integer []arr){
        if(k == 1){
            for (int i = 0; i < arr.length; i++) {
                tmpArr.add(arr[i]);
//                System.out.println(tmpArr.toString());
                result.add(new ArrayList<>(tmpArr));
                tmpArr.remove((Object)arr[i]);
            }
        }else if(k > 1){
            for (int i = 0; i < arr.length; i++) { //按顺序挑选一个元素
                tmpArr.add(arr[i]); //添加选到的元素
//                System.out.println(tmpArr.toString());
                result.add(new ArrayList<>(tmpArr));
                arrangement(k - 1, removeArrayElements(arr, tmpArr.toArray(new Integer[1]))); //没有取过的元素，继续挑选
                tmpArr.remove((Object)arr[i]);
            }
        }else{
            return ;
        }
    }

    /**
     * 移除数组某些元素（不影响原数组）
     * @param arr 数组
     * @param elements 待移除的元素
     * @return 剩余元素组成的新数组
     */
    public Integer[] removeArrayElements(Integer[] arr, Integer... elements){
        List<Integer> remainList = new ArrayList<>(arr.length);
        for(int i=0;i<arr.length;i++){
            boolean find = false;
            for(int j=0;j<elements.length;j++){
                if(arr[i] == elements[j]){
                    find = true;
                    break;
                }
            }
            if(!find){ //没有找到的元素保留下来
                remainList.add(arr[i]);
            }
        }
        Integer[] remainArray = new Integer[remainList.size()];
        for(int i=0;i<remainList.size();i++){
            remainArray[i] = remainList.get(i);
        }
        return remainArray;
    }
}
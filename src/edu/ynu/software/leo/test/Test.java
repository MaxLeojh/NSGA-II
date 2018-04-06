package edu.ynu.software.leo.test;

/**
 * Created by maxleo on 17-11-30.
 */
public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Integer num = (int)(Math.random() * 4);
            System.out.println(num);
        }
    }
}

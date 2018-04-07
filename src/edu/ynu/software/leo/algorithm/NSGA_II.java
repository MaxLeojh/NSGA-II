package edu.ynu.software.leo.algorithm;

import java.util.Comparator;

/**
 * Created by maxleo on 17-11-2.
 */
public class NSGA_II {

    public Population evolution (Population population) {
        Population result = new Population();
        Population newPopulation = population.crossover();//选择交叉 变异 生成新种群
//        System.out.println("crossover!");
        newPopulation.mutation();
//        System.out.println("mutation!");
        newPopulation.individualList.addAll(population.individualList);//并集
        newPopulation.calcDistance();//计算个体拥挤距离
        Integer rank = 0;
        while (result.size() < Population.populationSize) {
            System.out.println("rank "+rank);
            Population tempPopulation = new Population();
            tempPopulation.individualList = newPopulation.paretoFront();
            newPopulation.individualList.removeAll(tempPopulation.individualList);//去掉pareto front
            tempPopulation.setRanks(rank);
            int difference = Population.populationSize - result.size();
            if (tempPopulation.size() < difference) {//当pareto front能放得下
                result.individualList.addAll(tempPopulation.individualList);
            }
            else {//当pareto front放不下
                tempPopulation.individualList.sort(new distanceComparator());//按降序排列
                for (int i = 0; i < difference; i++) {
                    result.individualList.add(tempPopulation.individualList.get(i));
                }
            }
            rank++;
        }
        return result;
    }

    static class distanceComparator implements Comparator {
        public int compare(Object object1, Object object2) {// 实现接口中的方法
            Individual ind1 = (Individual) object1; // 强制转换
            Individual ind2 = (Individual) object2;
            return ind1.distance.compareTo(ind2.distance);
        }
    }
}

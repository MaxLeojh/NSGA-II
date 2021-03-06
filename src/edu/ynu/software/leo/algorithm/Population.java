package edu.ynu.software.leo.algorithm;

import edu.ynu.software.leo.dataSet.Iris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by maxleo on 17-11-2.
 */
public class Population {
    public static final Integer populationSize = 400;
    public static final Double crossRate = 0.5;
    public static final Double mutationRate = 0.001;

    public List<Individual> individualList = new ArrayList<>();

    public void setRanks(Integer rank) {
        for (int i = 0; i < individualList.size(); i++) {
            individualList.get(i).setRank(rank);
        }
    }

    public Population(Boolean isInitialize) { //initial population
        if (isInitialize) {
            for (int i = 0; i < populationSize; i++) {
                Individual newIndividual = new Individual(true);
                individualList.add(newIndividual);
            }
        }
    }

    public void geneGuide(){
        for (int i = 0; i < populationSize; i++) {
            individualList.get(i).geneGuide();
        }
    }

    public void eliteInjection(String filename) {
        List<Individual> eliteIndiv = getEliteIndiv(filename);
        for (int i = 0; i < eliteIndiv.size(); i++) {
            individualList.remove(0);
            individualList.add(eliteIndiv.get(i));
        }
    }

    public List<Individual> getEliteIndiv(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        List<Individual> eliteIndivs = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                String[] temp = tempString.split(",");
                Individual individual = new Individual();
                for (int i = 0; i < temp.length; i++) {
                    individual.gene.add(Integer.parseInt(temp[i]));
                }
                individual.calcDerivedAttr();
                eliteIndivs.add(individual);
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
        return eliteIndivs;
    }

    public Population() {
        new Population(false);
    }

    public Integer size(){
        return individualList.size();
    }

    public List<Individual> paretoFront () {
        List<Individual> result = new ArrayList<>();
        for (Individual ind :
                individualList) {
            if (isParetoOptimality(ind)) result.add(ind);
        }
        return result;
    }

    public boolean isParetoOptimality(Individual individual) {
        for (Individual ind:
             individualList) {
            if (individual.isDominatedBy(ind)) return false;
        }
        return true;
    }

    public Population crossover() {
        Random random = new Random();
        Population newPop = new Population();
        for (int i = 0; i < populationSize; i++) {
            Integer randomNum1 = random.nextInt(populationSize);
            Integer randomNum2 = random.nextInt(populationSize);
            Individual newInd = corss(individualList.get(randomNum1),individualList.get(randomNum2));
            newPop.individualList.add(newInd);
        }
        return newPop;
    }

    public void mutation() {
        Random random = new Random();
        for (Individual ind:individualList
             ) {
            if (random.nextDouble() < mutationRate) {
                Integer index = random.nextInt(Individual.geneSize);
                Integer content = random.nextInt(ind.clusterCount);
                ind.gene.set(index,content);
            }
        }
    }

    public Individual corss(Individual ind1, Individual ind2) {
        Random random = new Random();
        Individual newInd = new Individual();
        for (int i = 0; i < Individual.geneSize; i++) {
            if (random.nextDouble() < crossRate) {
                newInd.gene.add(ind1.gene.get(i));
            }
            else {
                newInd.gene.add(ind2.gene.get(i));
            }
        }
        newInd.calcDerivedAttr();
        return newInd;
    }

    public void calcDistance(){
        for (Individual ind :
                individualList) {
            ind.distance = 0.0;
        }
        for (int i = 0; i < Individual.objFunNum; i++) {
            individualList.sort(new adaptiveValuesComparator(i));//ascending order
            individualList.get(0).distance = Double.MAX_VALUE/(Individual.objFunNum*10);
            individualList.get(individualList.size()-1).distance = Double.MAX_VALUE/(Individual.objFunNum*10);
            for (int j = 1; j < size()-1; j++) {
                individualList.get(j).distance = individualList.get(j).distance +
                        (individualList.get(j+1).adaptiveValues.get(i) - individualList.get(j-1).adaptiveValues.get(i))/(individualList.get(individualList.size()-1).adaptiveValues.get(i) - individualList.get(0).adaptiveValues.get(i));
            }
        }
    }

    static class adaptiveValuesComparator implements Comparator {
        public Integer ojbFunIndex;

        public adaptiveValuesComparator(Integer ojbFunIndex) {
            this.ojbFunIndex = ojbFunIndex;
        }

        public int compare(Object object1, Object object2) {// 实现接口中的方法
            Individual ind1 = (Individual) object1; // 强制转换
            Individual ind2 = (Individual) object2;
            return ind1.adaptiveValues.get(ojbFunIndex).compareTo(ind2.adaptiveValues.get(ojbFunIndex));
        }
    }
}

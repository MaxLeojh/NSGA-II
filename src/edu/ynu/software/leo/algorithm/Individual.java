package edu.ynu.software.leo.algorithm;

import edu.ynu.software.leo.dataSet.Iris;
import edu.ynu.software.leo.test.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by maxleo on 17-11-2.
 */

public class Individual {
    public static final Integer geneSize = 150;
    public static final Integer objFunNum = 3;

    //Source attributes
    public List<Integer> gene = new ArrayList<>();
    public Integer classNum = 3;

    //Derived attributes
    public List<Double> adaptiveValues = new ArrayList<>();
    public List<Integer> clusterSizes = new ArrayList<>();
    public List<Iris> centroids = new ArrayList<>();

    public Integer rank = 0;//non-domainated rank
    public Double distance;//individual crowding distance

    public boolean isDominatedBy(Individual individual) {
        for (int i = 0; i < objFunNum; i++) {
            //if there is one adaptive values that the individual is not better than this
            if (individual.adaptiveValues.get(i) <= this.adaptiveValues.get(i))//the bigger the better.
                return false;
        }
        return true;
    }

    //calculate the adaptive values
    public void calcAV(Integer index) {
        //use case statement to control it.
    }

    public void calcAllAV() {
        for (int i = 0; i < objFunNum; i++) {
            calcAV(i);
        }
    }

    public void clacClusterSizes(){
        Integer count;
        Integer index;
        for (int i = 0; i < geneSize ; i++) {
            index = gene.get(i);
            count = clusterSizes.get(index);
            clusterSizes.set(index,count + 1);
        }
    }

    public void calcCentroids() {
        Iris[] tempSum = new Iris[classNum];
        for (int i = 0; i < classNum ; i++) {
            Iris iris = new Iris();
            tempSum[i] = iris;
        }
        for (int i = 0; i < geneSize ; i++) {
            Integer index = gene.get(i);
            tempSum[index].plus(Main.dataSet.get(i));
        }
        for (int i = 0; i < classNum ; i++) {
            tempSum[i].divideBy(clusterSizes.get(i));

            //reference:https://www.programcreek.com/2013/04/how-to-convert-array-to-arraylist-in-java/
            Collections.addAll(centroids,tempSum);
        }
    }

    /**
     * I'd like to put the objective function here in the Individual class
     * We need the Individual's gene and data set to calculate the objective value
     * Here, we can get gene directly, as for data set, I'd like to use FileReading function to make the control.
     * The difference between different data set is the calculation of distance
     * We could force every data set class to add a distance calculation function by using interface(doesn't work).
     */

    //<BEGIN>[Davies–Bouldin index]
    //based on wikipedia,reference:[https://en.wikipedia.org/wiki/Davies%E2%80%93Bouldin_index]
    public Double S(Integer i) {
        Iris centroid = centroids.get(i);

        Double sum = 0.0;
        for (int j = 0; j < geneSize; j++) {
            if (gene.get(j) == i) {
                sum += Math.pow(Main.dataSet.get(j).distance(centroid),2);
            }
        }
        sum /= clusterSizes.get(i);
        Double result = Math.pow(sum,1d/2);
        return result;
    }

    public Double M(Integer i, Integer j) {
        Double result = centroids.get(i).distance(centroids.get(j));
        return result;
    }

    public Double R(Integer i, Integer j) {
        Double result = (S(i)+S(j))/M(i,j);
        return result;
    }

    public Double D(Integer i) {
        Double result = 0.0;
        for (int j = 0; j < classNum ; j++) {
            if (i != j) {
                Double newResult = R(i,j);
                if (newResult > result) result = newResult;
            }
        }
        return result;
    }

    public Double DB() {
        Double sum = 0d;
        for (int i = 0; i < classNum ; i++) {
            sum += D(i);
        }
        Double result = sum/classNum;
        return result;
    }
    //<END>[Davies–Bouldin index]

}

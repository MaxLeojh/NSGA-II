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
    public static final Integer geneSize = 150; // for Iris data Set, Size is 150
    public static final Integer objFunNum = 3; // the number of object functions

    //Source attributes
    public List<Integer> gene = new ArrayList<>(); //one kind of gene, as well as one kind of cluster way
    public Integer clusterCount = 3; //the number of clusters;

    //Derived attributes
    public List<Double> adaptiveValues = new ArrayList<>();
    public List<Integer> clusterSizes = new ArrayList<>();
    public List<Iris> centroids = new ArrayList<>(); //centroid of every cluster

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
        Double value;
        switch (index){
            case 1: //[Davies–Bouldin index]
                System.out.println("case 1:");
                value = DB();
                System.out.println(value);
                adaptiveValues.add(1,value);
                break;
            case 2: //[Dunn index]
                System.out.println("case 2:");
                //HERE!
                break;
            case 3:
                System.out.println("case 3:");
                break;
            default:
                System.out.println("default");
                break;
        }
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
        Iris[] tempSum = new Iris[clusterCount];
        for (int i = 0; i < clusterCount ; i++) {
            Iris iris = new Iris();
            tempSum[i] = iris;
        }
        for (int i = 0; i < geneSize ; i++) {
            Integer index = gene.get(i);
            tempSum[index].plus(Main.dataSet.get(i));
        }
        for (int i = 0; i < clusterCount ; i++) {
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

    /**
     * <BEGIN>[Davies–Bouldin index]
     * based on wikipedia,reference:[https://en.wikipedia.org/wiki/Davies%E2%80%93Bouldin_index]
     */

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
        for (int j = 0; j < clusterCount ; j++) {
            if (i != j) {
                Double newResult = R(i,j);
                if (newResult > result) result = newResult;
            }
        }
        return result;
    }

    public Double DB() {
        Double sum = 0d;
        for (int i = 0; i < clusterCount ; i++) {
            sum += D(i);
        }
        Double result = sum/clusterCount;
        return result;
    }

    /**
     * <END>[Davies–Bouldin index]
     */

    /**
     * <BEGIN>[Dunn index]
     * based on wikipedia,reference:[https://en.wikipedia.org/wiki/Dunn_index]
     */

    //Inner-cluster distance--------------
    public Double maxDis(Integer i) { //the maximum distance
        ArrayList<Integer> cluster = new ArrayList<>();
        Double result = 0d;
        for (int j = 0; j < geneSize; j++)
            if (gene.get(j) == i) cluster.add(j); //find cluster i, put them into ArrayList 'cluster'
        for (int j = 0; j < cluster.size(); j++) { //calculate the distance of each pair
            for (int k = 0; k < j ; k++) {
                Double temp = Main.dataSet.get(cluster.get(j)).distance(Main.dataSet.get(cluster.get(k)));
                if (temp > result) result = temp;
            }
        }
        return result;
    }

    public Double meanDis(Integer i) { //the mean distance between all pairs
        ArrayList<Integer> cluster = new ArrayList<>();
        Double result = 0d;
        Integer count = 0;
        for (int j = 0; j < geneSize; j++)
            if (gene.get(j) == i) cluster.add(j); //find cluster i, put them into ArrayList 'cluster'
        for (int j = 0; j < cluster.size(); j++) {
            for (int k = 0; k < j ; k++) {
                Double temp = Main.dataSet.get(cluster.get(j)).distance(Main.dataSet.get(cluster.get(k))); //calculate the distance of each pair
                result += temp;
                count++;
            }
        }
        result = result/count;
        return result;
    }

    public Double meanPointDis(Integer i) { //distance of all the points from the mean
        ArrayList<Integer> cluster = new ArrayList<>();
        Double result = 0d;
        Iris centroid = centroids.get(i);
        for (int j = 0; j < geneSize; j++)
            if (gene.get(j) == i) cluster.add(j); //find cluster i, put them into ArrayList 'cluster'
        for (int j = 0; j < cluster.size(); j++) {
            Double temp = Main.dataSet.get(j).distance(centroid);
            result += temp;
        }
        result /= cluster.size();
        return null;
    }


    //Inter-cluster distance--------------
    public Double closestDis(Integer i, Integer j) {
        Double result = Double.MAX_VALUE;
        ArrayList<Integer> clusterI = new ArrayList<>();
        ArrayList<Integer> clusterJ = new ArrayList<>();
        for (int k = 0; k < geneSize; k++) { //find cluster i,j
            if (gene.get(k) == i) clusterI.add(k);
            if (gene.get(k) == j) clusterJ.add(k);
        }
        for (int k = 0; k < clusterI.size(); k++) {
            for (int l = 0; l < clusterJ.size(); l++) {
                Double temp = Main.dataSet.get(clusterI.get(k)).distance(Main.dataSet.get(clusterJ.get(l)));
                if (temp < result) result = temp;
            }
        }
        return result;
    }

    public Double farthestDis(Integer i, Integer j) {
        Double result = 0d;
        ArrayList<Integer> clusterI = new ArrayList<>();
        ArrayList<Integer> clusterJ = new ArrayList<>();
        for (int k = 0; k < geneSize; k++) { //find cluster i,j
            if (gene.get(k) == i) clusterI.add(k);
            if (gene.get(k) == j) clusterJ.add(k);
        }
        for (int k = 0; k < clusterI.size(); k++) {
            for (int l = 0; l < clusterJ.size(); l++) {
                Double temp = Main.dataSet.get(clusterI.get(k)).distance(Main.dataSet.get(clusterJ.get(l)));
                if (temp > result) result = temp;
            }
        }
        return result;
    }

    public Double centroidsDis(Integer i, Integer j) {
        Iris centroidI = centroids.get(i);
        Iris centroidJ = centroids.get(j);
        Double result = centroidI.distance(centroidJ);
        return result;
    }

    /**
     * <END>[Dunn index]
     */

    /**
     * <BEGIN>[Silhouette coefficient]
     * based on wikipedia,reference:[https://en.wikipedia.org/wiki/Silhouette_(clustering)]
     */

    public Double a(Integer i) { //the average distance inner cluster which contains sample i
        Integer clusterNo = gene.get(i); //get cluster number
        return aveDisBtSamClu(i,clusterNo);
    }

    public Double b(Integer i) {
        Double result = Double.MAX_VALUE;
        Integer resultCount = -1;
        Integer clusterNo = gene.get(i); //get cluster number
        for (int j = 0; j < clusterCount; j++) {
            if (j!=clusterNo){
                Double temp = aveDisBtSamClu(i,j);
                if (temp < result) {
                    result = temp;
                    resultCount = j;
                }
            }
        }

        return result;
    }

    public Double Sc(Integer i) {
        double a,b;
        a = a(i);
        b = b(i);
        Double result = (b-a)/Double.max(b,a);
        return result;
    }

    public Double aveDisBtSamClu(Integer sampleNo, Integer clusterNo){ //calculate the average distance between a sample and a cluster
        ArrayList<Integer> cluster = new ArrayList<>(); // container to store the elements from this cluster
        Double result = 0d;
        Integer count = 0;
        for (int j = 0; j < geneSize; j++)
            if (gene.get(j) == clusterNo) cluster.add(j); //find cluster clusterNum, put them into ArrayList 'cluster'
        for (int j = 0; j < cluster.size(); j++) {
            Double temp = Main.dataSet.get(cluster.get(j)).distance(Main.dataSet.get(sampleNo));
            result += temp;
            count++;
        }
        result = result/count;
        return result;
    }

    /**
     * <END>[Silhouette coefficient]
     */

}

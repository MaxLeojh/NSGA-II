package edu.ynu.software.leo.algorithm;

import edu.ynu.software.leo.dataSet.Iris;
import edu.ynu.software.leo.test.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by maxleo on 17-11-2.
 */

public class Individual {
    public static final Integer geneSize = 150; // 150 for Iris ,178 for Wine
    public static final Integer objFunNum = 3; // the number of object functions

    //Source attributes
    public List<Integer> gene = new ArrayList<>(); //one kind of gene, as well as one kind of cluster way
    public Integer clusterCount = 3; //the number of clusters;

    //Derived attributes
    public List<Double> adaptiveValues = new ArrayList<>();
    public List<Integer> clusterSizes = new ArrayList<>();
    public List<Iris> centroids = new ArrayList<>(); //centroid of every cluster

    public Integer rank = 0; //non-domainated rank
    public Double distance; //individual crowding distance

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Individual(Boolean isInitialize) {
        if (isInitialize) {
            for (int i = 0; i < geneSize; i++) {
                Integer randomNum = (int)(Math.random() * clusterCount);
                gene.add(i,randomNum);
            }
            calcDerivedAttr();
        }
    }

    public Individual() {
        new Individual(false);
    }

    public void calcDerivedAttr() { //full fill the derived Attributes
        calcClusterSizes();

        calcCentroids();

        calcAllAV();

    }

    public boolean isDominatedBy(Individual individual) {
        for (int i = 0; i < objFunNum; i++) {
            //if there is one adaptive values that the individual is not better than this
            if (individual.adaptiveValues.get(i) <= this.adaptiveValues.get(i)) //the bigger the better[Vital].
                return false;
        }
        return true;
    }

    public void calcAllAV() {
        for (int i = 0; i < objFunNum; i++) {
            calcAV(i);
        }


//        calcAV(2);

    }

    //calculate the adaptive values
    public void calcAV(Integer index) {
        //use case statement to control it.
        Double value;
        switch (index){
            case 0: //[Davies–Bouldin index]
                value = DB();
//                System.out.println("case 1:");
//                System.out.println("DB:"+value);
                adaptiveValues.add(-value);// get negative of DB, cause the bigger the better!
                break;
            case 1: //[Dunn index]
                value = DI();
//                System.out.println("case 2:");
//                System.out.println("DI:"+value);
                adaptiveValues.add(value);
                break;
            case 2:
                value = aveSc();//[Silhouette coefficient]
//                System.out.println("case 3:");
//                System.out.println("SC"+value);
                adaptiveValues.add(value);
                break;
            case 3:
                value = newIndex();//[new-index]
//                System.out.println("case 3:");
//                System.out.println("SC"+value);
                adaptiveValues.add(value);
                break;
            case 4:
                value = minCoveredDis();//[new-index]
//                System.out.println("case 3:");
//                System.out.println("SC"+value);
                adaptiveValues.add(value);
                break;
            default:
                System.out.println("case index ERROR!");
                break;
        }
    }


    public void calcClusterSizes(){
        Integer count;
        Integer index;
        for (int i = 0; i < clusterCount; i++) {
            clusterSizes.add(i,0);
        }
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
//                Double temp = Main.dataSet.get(cluster.get(j)).distance(Main.dataSet.get(cluster.get(k)));
                Double temp = Main.disMatrix[cluster.get(j)][cluster.get(k)];
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
//                Double temp = Main.dataSet.get(cluster.get(j)).distance(Main.dataSet.get(cluster.get(k))); //calculate the distance of each pair
                Double temp = Main.disMatrix[cluster.get(j)][cluster.get(k)];
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
//                Double temp = Main.dataSet.get(clusterI.get(k)).distance(Main.dataSet.get(clusterJ.get(l)));
                Double temp = Main.disMatrix[clusterI.get(k)][clusterJ.get(l)];
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
//                Double temp = Main.dataSet.get(clusterI.get(k)).distance(Main.dataSet.get(clusterJ.get(l)));
                Double temp = Main.disMatrix[clusterI.get(k)][clusterJ.get(l)];
                if (temp > result) result = temp;
            }
        }
        return result;
    }
    //------------------------------END-<Inter-cluster distance>-----------------------------------------------------------

    public Double centroidsDis(Integer i, Integer j) {
        Iris centroidI = centroids.get(i);
        Iris centroidJ = centroids.get(j);
        Double result = centroidI.distance(centroidJ);
        return result;
    }

    public Double minInterClusterDis(){
        Double result = Double.MAX_VALUE;
        for (int i = 0; i < clusterCount; i++) {
            for (int j = 0; j < i; j++) {
                Double temp = farthestDis(i,j);//choose the inter-cluster function
                if (temp < result) result = temp;
            }
        }
        return result;
    }

    public Double maxInnerClusterDis(){
        Double resutl = Double.MIN_VALUE;
        for (int i = 0; i < clusterCount; i++) {
            Double temp = meanDis(i); //choose the inner-cluster distance function
            if (temp > resutl) resutl = temp;
        }
        return resutl;
    }

    public Double DI(){ //Dunn index
        Double result;
        result = minInterClusterDis()/maxInnerClusterDis();
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


    public Double aveDisBtSamClu(Integer sampleNo, Integer clusterNo){ //calculate the average distance between a sample and a cluster
        ArrayList<Integer> cluster = new ArrayList<>(); // container to store the elements from this cluster
        Double result = 0d;
        Integer count = 0;
        for (int j = 0; j < geneSize; j++)
            if (gene.get(j) == clusterNo) cluster.add(j); //find cluster clusterNum, put them into ArrayList 'cluster'
        for (int j = 0; j < cluster.size(); j++) {
            Double temp = Main.dataSet.get(cluster.get(j)).distance(Main.dataSet.get(sampleNo));
//            Double temp = Main.disMatrix[cluster.get(j)][sampleNo];
            result += temp;
            count++;
        }
        result = result/count;
        return result;
    }

    public Double Sc(Integer i) {
        double a,b;
        a = a(i);
        b = b(i);
        Double result = (b-a)/Double.max(b,a);
        return result;
    }

    public Double aveSc(){
        Double result = 0d;
        for (int i = 0; i < geneSize; i++) {
            result += Sc(i);
        }
        result = result/geneSize;
        return result;
    }

    /**
     * <END>[Silhouette coefficient]
     */

    /**
     * <BEGIN>[new-index]
     * based on a paper.
     */

    public Double compact(Integer clusterNo) {
        System.out.println("inside the compact");
        Integer clusterSize = clusterSizes.get(clusterNo);
        if (clusterSize == 1) return 0d;
        else if (clusterSize == 2) return maxDis(clusterNo);
        System.out.println("clustersize:"+clusterSize);
        ArrayList<ArrayList<Integer>> patten = getPatten(clusterSize);
        ArrayList<Integer> Cluster = new ArrayList<>();
        for (int i = 0; i < gene.size(); i++) {
            if (gene.get(i)==clusterNo) Cluster.add(i);
        }
        Double result = 0d;
        for (int i = 0; i < clusterSize; i++) {
            for (int j = 0; j < i; j++) {
                Integer tempi = Cluster.get(i);
                Integer tempj = Cluster.get(j);
                ArrayList<Integer> currentCluster = new ArrayList<>(Cluster);
                currentCluster.remove(i);
                currentCluster.remove(j);
                currentCluster.add(0,tempi);
                currentCluster.add(0,tempj);
                Double dConnect = Double.MAX_VALUE;
                for (int k = 0; k < patten.size(); k++) {
                    ArrayList<Integer> curPatten = patten.get(k);
                    Double maxDistance = 0d;
                    for (int l = 0; l < curPatten.size()-1; l++) {
                        Double tempDis = Main.disMatrix[currentCluster.get(curPatten.get(l))][currentCluster.get(curPatten.get(l+1))];
                        if (tempDis > maxDistance) maxDistance = tempDis;
                    }
                    if (maxDistance < dConnect) dConnect = maxDistance;
                }
                if (dConnect > result) result = dConnect;

            }
        }

        return 1/result;
    }

    public Double dist(Integer ci, Integer cj) {
        return closestDis(ci,cj);
    }

    public Double index(Integer ci) {
        Double result = Double.MAX_VALUE;
        for (int i = 0; i < clusterCount; i++) {
            if (i != ci) {
                Integer sizeI = clusterSizes.get(i);
                Integer sizeCi = clusterSizes.get(ci);
                Double temp = dist(ci,i)*((sizeCi*compact(ci)+sizeI*compact(i))/(sizeCi+sizeI));
                if (temp < result) result = temp;
            }
        }
        return result;
    }

    public Double newIndex() {
        Double result = Double.MAX_VALUE;
        for (int i = 0; i < clusterCount; i++) {
            Double temp = index(i);
            if (temp < result) result = temp;
        }

        return result;
    }

    public ArrayList<ArrayList<Integer>> getPatten(Integer clusterSize) {
        if (clusterSize < 3) return null;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(0);
        temp.add(1);
        result.add(temp);
        Integer size = clusterSize-2;
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(2+i);
        }
        Integer [] com = (Integer []) (array.toArray(new Integer[size]));
        System.out.println("debug 1");
        CombineAndArrangement caa = new CombineAndArrangement();
        caa.arrangement(size,com);
        System.out.println("debug 2");
        for (int i = 0; i < caa.result.size(); i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            tmp.add(0);
            tmp.addAll(caa.result.get(i));
            tmp.add(1);
            result.add(tmp);
        }
        return result;
    }


    /**
     * <END>[Silhouette coefficient]
     */

    /**
     * <BEGIN>[min-cover-distance]
     * based on a paper.
     */
    public Double minCoveredDis() {
        Double result = 0d;
        for (int i = 0; i < clusterCount; i++) {
            CoverDistance coverDistance = new CoverDistance(gene,i,1.6,0.0,0.01);
            Double temp = coverDistance.calcDis();
            result += temp;
        }
        result = result/clusterCount;
        return -result;
    }

    /**
     * <END>[min-cover-distance]
     */

    public Integer farthestPoint(Integer i) {
        ArrayList<Integer> cluster = new ArrayList<>();
        Double maxDis = 0d;
        Integer result = Integer.MAX_VALUE;
        for (int j = 0; j < geneSize; j++)
            if (gene.get(j) == i) cluster.add(j); //find cluster i, put them into ArrayList 'cluster'
        for (int j = 0; j < cluster.size(); j++) {
            Double temp = centroids.get(i).distance(Main.dataSet.get(cluster.get(j)));
            if (temp >= maxDis) {
                maxDis = temp;
                result = cluster.get(j);
            }
        }
        return result;
    }

    public void geneGuide() {
        for (int i = 0; i < clusterCount; i++) {
            Integer geneNo = farthestPoint(i);
            KNN(geneNo, 10);
        }
    }

    public void KNN(Integer geneNo, Integer N){ //be careful of N, which can not be bigger than the size of gene
        Integer[] counter = new Integer[clusterCount];
        Integer comp = Integer.MIN_VALUE;
        Integer newLabel = Integer.MAX_VALUE;
        for (int i = 0; i < clusterCount; i++) {
            counter[i] = 0;
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < geneSize; i++) {
            list.add(i);
        }

        list.sort(new distanceComparator(geneNo)); //Ascending order

        for (int i = 0; i < N; i++) {
            Integer clusterLabel = gene.get(list.get(i));
            counter[clusterLabel] ++;
        }

        for (int i = 0; i < clusterCount; i++) {
            if (counter[i] > comp) {
                comp = counter[i];
                newLabel = i;
            }
        }

        gene.set(geneNo,newLabel);
    }

    static class distanceComparator implements Comparator {
        public Integer index0;

        public distanceComparator(Integer index0) {
            this.index0 = index0;
        }

        @Override
        public int compare(Object o1, Object o2) {
            Integer index1 = (Integer) o1;
            Integer index2 = (Integer) o2;
            return Main.disMatrix[index0][index1].compareTo(Main.disMatrix[index0][index2]);//Ascending order
        }
    }

}

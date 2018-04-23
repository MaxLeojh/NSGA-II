package edu.ynu.software.leo.algorithm;

import edu.ynu.software.leo.test.Main;

import java.util.ArrayList;
import java.util.List;

public class CoverDistance {
    public ArrayList<Boolean> cluster = new ArrayList<>();
    public Boolean [] isCovered;
    public Integer size;

    public Double head;
    public Double tail;
    public Double gap;
    public Integer startPoint;

    public CoverDistance(List<Integer> gene, Integer clusterNo,Double head, Double tail, Double gap) {
        for (int i = 0; i < gene.size(); i++) {
            if (gene.get(i)==clusterNo) {
                cluster.add(true);
                startPoint = i;
            }
            else {
                cluster.add(false);
            }
        }
        size = cluster.size();
        isCovered = new Boolean[size];
        initializeIsCovered();
        this.head = head;
        this.tail = tail;
        this.gap = gap;
    }

    public void initializeIsCovered(){
        for (int i = 0; i < size; i++) {
            isCovered[i] = false;
        }
    }

    public Double calcDis() {
        while (tail-head > gap) {
            Double tempDis = head+(tail-head)/2;
            initializeIsCovered();
            floodFill(tempDis,startPoint);
            Boolean result = isAllCovered();
            if (result) tail = tempDis;
            else head = tempDis;
        }
        return tail;
    }

    public void floodFill(Double covDis, Integer point) {
        for (int i = 0; i < size; i++) {
            if (cluster.get(i)&&!isCovered[i]) {
                Double dis = Main.disMatrix[i][point];
                if (dis < covDis) {
                    isCovered[i] = new Boolean(true);
                    floodFill(covDis,i);
                }
            }
        }
    }

    public Boolean isAllCovered() {
        for (int i = 0; i < size; i++) {
            if (!isCovered[i]) return false;
        }
        return true;
    }

}

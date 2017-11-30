package edu.ynu.software.leo.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxleo on 17-11-2.
 */

public class Individual {
    public static final Integer geneSize = 150;
    public static final Integer objFunNum = 3;

    public List<Integer> gene = new ArrayList<>();
    public Integer classNum = 3;
    public List<Double> adaptiveValues = new ArrayList<>();

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

}

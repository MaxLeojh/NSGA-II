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

    public void calcAV(Integer index) {
        //use case statement to control it.
    }

    public void calcAllAV() {
        for (int i = 0; i < objFunNum; i++) {
            calcAV(i);
        }
    }

    /**
     * I'd like to put the objective function here in the Individual class
     * We need the Individual's gene and data set to calculate the objective value
     * Here, we can get gene directly, as for data set, I'd like to use FileReading function to make the control.
     * The difference between different data set is the calculation of distance
     * We could force every data set class to add a distance calculation function by using interface.
     */

    //based on wikipedia
    public Double DBI() {

        return null;
    }

    public Double S(Integer i) {

        return null;
    }

    public Double M(Integer i, Integer j) {

        return null;
    }

    public Double R(Integer i, Integer j) {

        return null;
    }

}

package edu.ynu.software.leo.dataSet;

/**
 * Created by maxleo on 17-11-16.
 */
public class Iris {
    /**
     * Attribute Information:
     * 1. sepal length in cm
     * 2. sepal width in cm
     * 3. petal length in cm
     * 4. petal width in cm
     * 5. class:
        -- Iris Setosa
        -- Iris Versicolour
        -- Iris Virginica
     */
    public Double sepalL;
    public Double sepalW;
    public Double petalL;
    public Double petalW;
    public String type;

    public Double distance(Iris obj) {
        return Math.sqrt(Math.pow(this.sepalL-obj.sepalL,2) + Math.pow(this.sepalW-obj.sepalW,2) + Math.pow(this.petalL-obj.petalL,2) + Math.pow(this.petalW-obj.petalW,2));
    }
}

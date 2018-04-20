import java.util.ArrayList;

public class Result {

    private Mat nodes;
    private Mat matr;
    private double value = 0;
    private double error = 0;
    private ArrayList<Double> function;
    private ArrayList<Double> errors;
    private int steps = 0;

    Result(Mat nodes, Mat matr, double value, double error, ArrayList function, ArrayList errors, int steps) {
        this.nodes = nodes;
        this.matr = matr;
        this.value = value;
        this.error = error;
        this.function = function;
        this.errors = errors;
        this.steps = steps;
    }

    public double getValue() {
        return value;
    }

    public double getError() {
        return error;
    }

    public int getSteps() {
        return steps;
    }

    public ArrayList<Double> getFunction() {
        return function;
    }

    public ArrayList<Double> getErrors() {
        return errors;
    }

    public Mat getNodes() {
        return nodes;
    }

    public Mat getMatr() {
        return matr;
    }

    public String getResults() {
        return "*****РЕЗУЛЬТАТЫ РАБОТЫ АЛГОРИТМА*****\nАргументы функции: \n"+nodes
                + "\nМатрица: \n"+matr + "\nЗначение функции: " + value + "\nОшибка: "+error+"\nКол-во шагов: "+steps+"\n";
    }

}

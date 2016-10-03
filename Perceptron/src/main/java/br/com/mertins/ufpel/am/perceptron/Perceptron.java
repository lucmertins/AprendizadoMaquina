package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Perceptron {

    private final List<Sinaps> sinapsList = new ArrayList<>();
    private double bias;

    public Perceptron(double bias) {
        this.bias = bias;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void addIn(double in) {
        sinapsList.add(new Sinaps(in, 1));
    }

    public void addIn(double in, double weight) {
        sinapsList.add(new Sinaps(in, weight));
    }

    double sum() {
        double result = 1 * bias;
        result = sinapsList.stream().map((sin) -> sin.getIn() * sin.getWeight()).reduce(result, (accumulator, _item) -> accumulator + _item);
        return result;
    }
}

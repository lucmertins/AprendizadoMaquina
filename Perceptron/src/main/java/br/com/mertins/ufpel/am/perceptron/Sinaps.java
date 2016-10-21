package br.com.mertins.ufpel.am.perceptron;

import java.io.Serializable;

/**
 *
 * @author mertins
 */
public class Sinaps implements Serializable {

    private double in;
    private double weight;

    public Sinaps(double in, double weight) {
        this.in = in;
        this.weight = weight;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}

package br.com.mertins.ufpel.am.perceptron;

import java.io.Serializable;

/**
 *
 * @author mertins
 */
public class Sinaps implements Serializable {

    private static final long serialVersionUID = 176732176609505155L;
    private double in;
    private double weight;
    private double delta;

    public Sinaps(double in, double weight) {
        this.in = in;
        this.weight = weight;
        this.delta = 0;
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

    public double getDelta() {
        return delta;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        this.delta = 0;
    }

    public void updateWeight(double delta) {
        this.delta = delta;
        this.weight += delta;
    }

}

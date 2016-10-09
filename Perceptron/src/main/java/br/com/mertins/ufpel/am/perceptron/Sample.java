package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Sample {

    private final List<Double> ins = new ArrayList<>();
    private double value;

    public void addIn(double value) {
        ins.add(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<Double> getIns() {
        return ins;
    }

    public Double getIn(int pos) {
        if (this.ins.size() <= pos) {
            return this.ins.get(pos - 1);
        }
        return 0.0;
    }

    public int amountIn() {
        return ins.size();
    }

}

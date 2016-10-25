package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Sample {

    private final List<Double> ins = new ArrayList<>();
//    private double value;
    private final List<Double> outs = new ArrayList<>();
    private boolean normalize;

    public Sample() {
    }

    public Sample(boolean normalize) {
        this.normalize = normalize;
    }

    public void addIn(double value) {
        ins.add(this.normalize ? value > 0 ? 1.0 : 0 : value);
    }

    public void addOut(double value) {
        outs.add(value);
    }

//    public double getValue() {
//        return value;
//    }
//
//    public void setValue(double value) {
//        this.value = value;
//    }
    public List<Double> getIns() {
        return ins;
    }

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
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

    public int amountOut() {
        return outs.size();
    }

    public void fill(Perceptron perceptron) {
        int pos = 1;
        for (Double vtemp : ins) {
            perceptron.updateIn(pos++, vtemp);
        }
    }

    public Double getOut(int pos) {
        if (this.outs.size() <= pos) {
            return this.outs.get(pos - 1);
        }
        return 0.0;
    }

    public String toStringIn() {
        StringBuilder sb = new StringBuilder(" ");
        this.ins.forEach(value -> {
            sb.append(String.format("%.0f ", value));
        });
        return sb.toString().trim();
    }

    public String toStringOut() {
        StringBuilder sb = new StringBuilder(" ");
        this.outs.forEach(value -> {
            sb.append(String.format("%.0f ", value));
        });
        return sb.toString().trim();
    }

}

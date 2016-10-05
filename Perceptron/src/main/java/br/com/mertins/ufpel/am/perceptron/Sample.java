package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Sample {

    private final List<Double> values = new ArrayList<>();
    private int value;

    public void addIn(double value) {
        values.add(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Double> getValues() {
        return values;
    }

    public int amountIn() {
        return values.size();
    }

}

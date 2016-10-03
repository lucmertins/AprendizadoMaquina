package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Perceptron {

    private final List<Sinaps> sinaps = new ArrayList<>();

    public void addIn(double in) {
        sinaps.add(new Sinaps(in, 1));
    }

    public void addIn(double in, double weight) {
        sinaps.add(new Sinaps(in, weight));
    }
}

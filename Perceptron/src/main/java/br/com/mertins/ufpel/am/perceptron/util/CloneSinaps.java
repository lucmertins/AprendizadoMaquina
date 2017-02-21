package br.com.mertins.ufpel.am.perceptron.util;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sinaps;
import java.io.Serializable;

/**
 *
 * @author mertins
 */
public class CloneSinaps implements Serializable {

    private static final long serialVersionUID = -1L;
    private double in;
    private double weight;
    private double delta;

    public CloneSinaps(Sinaps sinaps) {
        this.in = sinaps.getIn();
        this.weight = sinaps.getWeight();
        this.delta = sinaps.getDelta();
    }

    public Sinaps desfazClone() {
        Sinaps sinaps = new Sinaps(in, weight);
        sinaps.setDelta(delta);
        return sinaps;
    }
}

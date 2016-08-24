package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import java.io.Serializable;

/**
 *
 * @author mertins
 */
public class Node implements Serializable {

    private final Attribute attribute;
    private final double gain;

    public Node(Attribute attribute, double gain) {
        this.attribute = attribute;
        this.gain = gain;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getGain() {
        return gain;
    }

    @Override
    public String toString() {
        return String.format("Node {attribute= %s  gain=%f}", attribute, gain);
    }
}

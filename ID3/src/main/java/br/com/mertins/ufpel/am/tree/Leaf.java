package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Label;

/**
 *
 * @author mertins
 */
public class Leaf extends Node {
    private final Label label;
    
    public Leaf(Attribute attribute,Label label) {
        super(attribute, 0);
        this.label=label;
    }
    @Override
    public String toString() {
        return String.format("Leaf {attribute= %s label=%s positive=%d negative=%d}", this.getAttribute(), label,this.getPositive(),this.getNegative());
    }
}

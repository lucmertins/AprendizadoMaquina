package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Label;

/**
 *
 * @author mertins
 */
public class Leaf extends NodeBase {

    private final Label label;

    public Leaf(ElementValue label) {
        this.label = (Label) label;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("Leaf label=%s instance=%s}", label, this.getAttributeInstanceParent() == null ? "" : this.getAttributeInstanceParent().getValue());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && this.label.equals(((Leaf) obj).label);
    }
}

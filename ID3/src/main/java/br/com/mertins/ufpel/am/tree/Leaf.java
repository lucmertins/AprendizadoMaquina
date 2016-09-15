package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Label;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author mertins
 */
public class Leaf extends NodeBase {

    private final Label label;

    public Leaf(ElementValue label) {
        this.label = (Label) label;
    }

    public Leaf(ElementValue label, Map<Label, BigDecimal> newSumary) {
        this.label = (Label) label;
        this.sumary.clear();
        newSumary.keySet().forEach(labelSumary -> {
            this.sumary.put(labelSumary, newSumary.get(labelSumary));
        });
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

    @Override
    protected NodeBase copy() {
        Leaf newLeaf = new Leaf(this.label);
        super.copy(newLeaf);
        return newLeaf;
    }
}

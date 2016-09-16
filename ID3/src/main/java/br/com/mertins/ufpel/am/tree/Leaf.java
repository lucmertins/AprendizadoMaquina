package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Label;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

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
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Leaf other = (Leaf) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        if (!Objects.equals(this.getParent(), other.getParent())) {
            return false;
        }
        return true;
    }

    @Override
    protected NodeBase copy() {
        Leaf newLeaf = new Leaf(this.label);
        super.copy(newLeaf);
        return newLeaf;
    }
}

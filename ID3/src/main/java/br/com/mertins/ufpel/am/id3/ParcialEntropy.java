package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Label;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class ParcialEntropy implements Serializable {

    private Attribute attribute;
    private Label label;
    private long sum;

    public ParcialEntropy(Attribute attribute, Label label) {
        this.attribute = attribute;
        this.label = label;
    }

    public void add() {
        sum++;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.attribute);
        hash = 41 * hash + Objects.hashCode(this.label);
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
        final ParcialEntropy other = (ParcialEntropy) obj;
        if (!Objects.equals(this.attribute, other.attribute)) {
            return false;
        }
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("ParcialEntropy {attribute= %s  label=%s  sum=%d}", attribute, label, sum);
    }
}

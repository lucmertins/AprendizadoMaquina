package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Label implements ElementValue, Serializable {

    private String value;

    public Label(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.value);
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
        final Label other = (Label) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.format("Label {value= %s}", value);
    }

    @Override
    public ElementValue clone() {
        try {
            return (ElementValue) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Label.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ElementValue copy() {
        return new Label(this.value);
    }

}

package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Label implements Serializable {

    private String value;
    private boolean positive;

    public Label(String value,String instancePositive) {
        this.value = value;
        this.positive = value.trim().equalsIgnoreCase(instancePositive.trim());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
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
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Label {value= %s  %s}", value, this.positive ? "Positive" : "Negative");
    }

}

package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Rotulo implements Serializable {

    private String value;

    public Rotulo(String value) {
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
        final Rotulo other = (Rotulo) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Rotulo {value= %s}", value);
    }

}

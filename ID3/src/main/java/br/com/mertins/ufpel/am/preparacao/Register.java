package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Register implements Serializable {

    private Long line;
    private List<Attribute> attributes;

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.line);
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
        final Register other = (Register) obj;
        return Objects.equals(this.line, other.line);
    }

}

package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Register implements Serializable {

    private final Long line;
    private final List<AttributeInstance> attributesInstance =new ArrayList();

    public Register(Long line) {
        this.line = line;
    }

    public List<AttributeInstance> getAttributesInstance() {
        return attributesInstance;
    }

    public void addAttributesInstance(AttributeInstance attributesInstance) {
        this.attributesInstance.add(attributesInstance);
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

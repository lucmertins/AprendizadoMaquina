package br.com.mertins.ufpel.am.preparacao;

import java.util.Objects;

/**
 *
 * @author mertins
 */
public class AttributeInstance {

    private String value;
    private Attribute attribute;

    public AttributeInstance() {
    }

    public AttributeInstance(Attribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public AttributeInstance copy() {
        return new AttributeInstance(attribute.copy(), value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.value);
        hash = 53 * hash + Objects.hashCode(this.attribute);
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
        final AttributeInstance other = (AttributeInstance) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.attribute, other.attribute)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("AttributeInstance {attribute = %s value= %s}", attribute.toString(), value);
    }
}

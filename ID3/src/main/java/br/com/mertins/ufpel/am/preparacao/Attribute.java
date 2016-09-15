package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Attribute implements ElementValue, Serializable {

    private final int position;
    private final String name;
    private final Set<AttributeInstance> attributesInstance = new HashSet<>();

    public Attribute(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Set<AttributeInstance> getAttributesInstance() {
        return attributesInstance;
    }

    public void addAttributeInstance(AttributeInstance attributeInstance) {
        this.attributesInstance.add(attributeInstance);
    }

    public AttributeInstance addAttributeInstance(String value) {
        AttributeInstance temp = new AttributeInstance(this, value);
        for (AttributeInstance instance : attributesInstance) {
            if (temp.equals(instance)) {
                return instance;
            }

        }
        this.attributesInstance.add(temp);
        return temp;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.position;
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
        final Attribute other = (Attribute) obj;
        return this.position == other.position;
    }

    @Override
    public String toString() {
        return String.format("attribute{%s}", name);
    }

}

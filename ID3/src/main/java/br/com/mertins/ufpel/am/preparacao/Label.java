package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Label implements ElementValue,Serializable {

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

//    public static Label positive(Label label,Set<Label> labels) {
//        for (Label labelTemp : labels) {
//            if (labelTemp.equals(label)) {
//                return label;
//            }
//        }
//        return null;
//    }
//
//    public static Label negative(Set<Label> labels) {
//        for (Label label : labels) {
//            if (!label.isPositive()) {
//                return label;
//            }
//        }
//        return null;
//    }
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

}

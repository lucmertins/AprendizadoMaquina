package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Register implements Serializable {

    private final Long line;
    private final List<AttributeInstance> attributesInstance = new ArrayList();
    private Label label;

    public Register(Long line) {
        this.line = line;
    }

    public Long getLine() {
        return line;
    }

    public List<AttributeInstance> getAttributesInstance() {
        return attributesInstance;
    }

    public void addAttributesInstance(AttributeInstance attributesInstance) {
        this.attributesInstance.add(attributesInstance);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public static Label dominant(List<Register> registers) {

        Map<Label, BigDecimal> valores = new HashMap<>();
        double size = registers.size();
        registers.stream().forEach((register) -> {
            if (valores.containsKey(register.getLabel())) {
                valores.put(register.getLabel(), valores.get(register.getLabel()).add(BigDecimal.ONE));
                valores.get(register.getLabel());
            } else {
                valores.put(register.getLabel(), BigDecimal.ONE);
            }
        });
        Label domi = null;
        long total = -1;
        for (Label label : valores.keySet()) {
            if (valores.get(label).longValue() > total) {
                domi = label;
                total = valores.get(label).longValue();
            }
        }
        return domi;
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

//    public static Label getLabelPositive(List<Register> registros) {
//        for (Register registro : registros) {
//            if (registro.getLabel().isPositive()) {
//                return registro.getLabel();
//            }
//        }
//        return null;
//    }
//
//    public static Label getLabelNegative(List<Register> registros) {
//        for (Register registro : registros) {
//            if (!registro.getLabel().isPositive()) {
//                return registro.getLabel();
//            }
//        }
//        return null;
//    }
}

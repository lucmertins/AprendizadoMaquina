package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mertins
 */
public class Entropy {

    public static double calc(List<Register> set) {
        Map<Label, BigDecimal> valores = new HashMap<>();
        double size = set.size();
        set.stream().forEach((register) -> {
            if (valores.containsKey(register.getLabel())) {
                valores.put(register.getLabel(), valores.get(register.getLabel()).add(BigDecimal.ONE));
                valores.get(register.getLabel());
            } else {
                valores.put(register.getLabel(), BigDecimal.ONE);
            }
        });
        double result = 0;
        result = valores.keySet().stream().map((label)
                -> valores.get(label).longValue()).map((value)
                -> -(value / size * log2(value / size))).reduce(result, (accumulator, _item) -> accumulator + _item);

        return result;
    }

    public static double calc(List<Register> set, AttributeInstance attributeInstance) {
        Map<Label, BigDecimal> valores = new HashMap<>();
        double total = 0D;
        for (Register register : set) {
            for (AttributeInstance attribInst : register.getAttributesInstance()) {
                if (attributeInstance.equals(attribInst)) {
                    if (valores.containsKey(register.getLabel())) {
                        valores.put(register.getLabel(), valores.get(register.getLabel()).add(BigDecimal.ONE));
                        valores.get(register.getLabel());
                    } else {
                        valores.put(register.getLabel(), BigDecimal.ONE);
                    }
                    total++;
                }
            }
        }
        final double size = total;
        double result = 0;
        result = valores.keySet().stream().map((label)
                -> valores.get(label).longValue()).map((value)
                -> -(value / size * log2(value / size))).reduce(result, (accumulator, _item) -> accumulator + _item);
        return result;

    }

    private static double log2(double value) {
        double ret = Math.log(value) / Math.log(2);
        return ret == Double.NaN ? 0 : ret == Double.NEGATIVE_INFINITY ? 0 : ret == Double.POSITIVE_INFINITY ? 0 : ret;
    }

}

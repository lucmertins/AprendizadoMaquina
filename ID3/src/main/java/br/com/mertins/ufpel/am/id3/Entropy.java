package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Entropy {

    public static double calc(List<Register> set) {
        double positivos = 0, negativos = 0, size = set.size();
        for (Register register : set) {
            if (register.getLabel().isPositive()) {
                positivos++;
            } else {
                negativos++;
            }
        }
        return -(positivos / size * log2(positivos / size)) - (negativos / size * log2(negativos / size));
    }

    public static double calc(List<Register> set, AttributeInstance attributeInstance) {
        double positivos = 0, negativos = 0;
        for (Register register : set) {
            for (AttributeInstance attribInst : register.getAttributesInstance()) {
                if (attributeInstance.equals(attribInst)) {
                    if (register.getLabel().isPositive()) {
                        positivos++;
                    } else {
                        negativos++;
                    }
                }
            }
        }
        double total = positivos + negativos;
        return -(positivos / total * log2(positivos / total)) - (negativos / total * log2(negativos / total));
    }

    private static double log2(double value) {
        double ret = Math.log(value) / Math.log(2);
        return ret == Double.NaN ? 0 : ret == Double.NEGATIVE_INFINITY ? 0 : ret == Double.POSITIVE_INFINITY ? 0 : ret;
    }
}

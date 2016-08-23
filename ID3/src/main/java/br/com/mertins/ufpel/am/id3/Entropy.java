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
        return -(positivos / size * (Math.log(positivos / size) / Math.log(2))) - (negativos / size * (Math.log(negativos / size) / Math.log(2)));
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
        return -(positivos / total * (Math.log(positivos / total) / Math.log(2))) - (negativos / total * (Math.log(negativos / total) / Math.log(2)));
    }
}

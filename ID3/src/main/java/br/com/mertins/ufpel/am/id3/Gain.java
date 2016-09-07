package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.util.List;

/**
 * página 60 do livro informa os resultados para conferencia.
 *
 * @author mertins
 */
public class Gain {

    private Gain() {
    }

    public static double calc(List<Register> set, ElementValue attribute) {
        double entropiaTotal = Entropy.calc(set);
        if (entropiaTotal > 0) {
            int total = set.size();
            for (AttributeInstance attributeInstance : ((Attribute)attribute).getAttributesInstance()) {
                double totalAttributeInstance = Gain.totalSet(set, attributeInstance);
                double entropyInstance = Entropy.calc(set, attributeInstance);
                entropiaTotal -= totalAttributeInstance / total * entropyInstance;
//            System.out.printf(" %s = %f   de  %d    entropyInstance=%f      total = %f\n",attributeInstance,totalAttributeInstance,total,entropyInstance,totalAttributeInstance / total * entropyInstance);
            }
        }
        return entropiaTotal;
    }

//    public static long positivos(List<Register> set) {
//        long positivos = 0;
//        for (Register registro : set) {
//            if (registro.getLabel().isPositive()) {
//                positivos++;
//            }
//        }
//        return positivos;
//    }
//
//    public static long positivos(List<Register> set, AttributeInstance attributeInstance) {
//        long positivos = 0;
//        for (Register registro : set) {
//            for (AttributeInstance instance : registro.getAttributesInstance()) {
//                if (attributeInstance.equals(instance)) {
//                    if (registro.getLabel().isPositive()) {
//                        positivos++;
//                    }
//
//                }
//            }
//        }
//        return positivos;
//    }

//    public static long negativos(List<Register> set) {
//        long negativos = 0;
//        for (Register registro : set) {
//            if (!registro.getLabel().isPositive()) {
//                negativos++;
//            }
//        }
//        return negativos;
//    }

//    public static long negativos(List<Register> set, AttributeInstance attributeInstance) {
//        long negativos = 0;
//        for (Register registro : set) {
//            for (AttributeInstance instance : registro.getAttributesInstance()) {
//                if (attributeInstance.equals(instance)) {
//                    if (!registro.getLabel().isPositive()) {
//                        negativos++;
//                    }
//
//                }
//            }
//        }
//        return negativos;
//    }

    private static double totalSet(List<Register> set, AttributeInstance attributeInstance) {
        long total = 0;
        for (Register registro : set) {
            for (AttributeInstance instance : registro.getAttributesInstance()) {
                if (attributeInstance.equals(instance)) {
                    total++;
                }
            }
        }
        return total;
    }
}

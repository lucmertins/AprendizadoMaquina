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

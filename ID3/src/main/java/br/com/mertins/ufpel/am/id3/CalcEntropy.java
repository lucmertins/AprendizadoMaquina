package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class CalcEntropy {

    private final List<Register> registers;
    private final Set<Label> labels;

    private Set<ParcialEntropy> resultParcial = new HashSet<>();

    public CalcEntropy(List<Register> registers, Set<Label> labels) {
        this.registers = registers;
        this.labels = labels;
    }

    public void process() {
        registers.forEach(register -> {
            register.getAttributesInstance().forEach(attributeInstance -> {

                ParcialEntropy parcial = new ParcialEntropy(attributeInstance.getAttribute(), register.getLabel());
                if (resultParcial.contains(parcial)) {
                    resultParcial.forEach(parcialEntropy -> {
                        if (parcial.equals(parcialEntropy)) {
                            parcialEntropy.add();
                        }
                    });
                } else {
                    parcial.add();
                    resultParcial.add(parcial);
                }
            });
            Attribute attribute = register.getAttributesInstance().get(0).getAttribute();

//            System.out.printf("%d   %s  %s\n", register.getLine(), register.getAttributesInstance().get(0).toString(), register.getLabel().toString());
        });
//        labels.forEach(label -> {
//            System.out.printf("\t\t%s\n", label.getValue());
//        });

        this.resultParcial.forEach(action -> {
            System.out.println(action.toString());
        });
    }

}

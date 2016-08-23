package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
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

    private Map<Attribute, Double> result;

    public CalcEntropy(List<Register> registers,Set<Label> labels) {
        this.registers = registers;
        this.labels=labels;
    }
    
    public void process(){
        registers.forEach(register->{
            
            register.getAttributesInstance().forEach(attributeInstance->{
                attributeInstance.getAttribute().getAttributesInstance();
            });
            System.out.printf("%d   %s  %s\n", register.getLine(), register.getAttributesInstance().get(0).toString(), register.getLabel().toString());
        });
        labels.forEach(label->{
            System.out.printf("\t\t%s\n",label.getValue());
        });
    }

}

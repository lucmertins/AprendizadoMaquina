package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.NodeRaiz;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class ID3 {

    private final List<Register> registers;
    private final List<Attribute> attributes;
    private final Set<Label> labels;

    public ID3(List<Register> registers, List<Attribute> attributes, Set<Label> labels) {
        this.registers = registers;
        this.attributes = attributes;
        this.labels = labels;
    }

    public void process() {
        NodeRaiz root = null;
        double calcMax = 0;
        for (Attribute attribute : this.attributes) {
            double calc = Gain.calc(registers, attribute);
            if (calcMax < calc) {
                root = new NodeRaiz(attribute, calc);
                calcMax = calc;
            }
            //System.out.printf("%s = %f\n", attribute, calc);
        }

//        System.out.println(root);
//        root.getAttribute().getAttributesInstance().forEach(action->{
//            System.out.println(action);
//        });
        if (root != null) {
            for (AttributeInstance attributeInstance : root.getAttribute().getAttributesInstance()) {
                List<Register> subconjunto = this.subconjunto(registers, root.getAttribute(), attributeInstance);
                System.out.println("****");
                subconjunto.forEach(action -> {
                    System.out.printf("%d  %s  %s\n", action.getLine(), action.getAttributesInstance().get(0), action.getLabel());
                }
                );

            }
//            List<Register> avalRegister = this.registers;
//            List<Attribute> avalAttributes = this.attributes;
//            for (Attribute attribute : avalAttributes) {
//                double calc = Gain.calc(avalRegister, attribute);
//                if (calcMax < calc) {
////                    root = new NodeRaiz(attribute, calc);
////                    calcMax = calc;
//                }
//                //System.out.printf("%s = %f\n", attribute, calc);
//            }
        } else {
            System.out.println("FIM sem ROOT");
        }

    }

    private List<Register> subconjunto(List<Register> avalRegister, Attribute attribute, AttributeInstance attributeInstance) {
        List<Register> retorno = new ArrayList<>();
        avalRegister.forEach(register -> {
            register.getAttributesInstance().forEach(attInst -> {
                if (attInst.equals(attributeInstance) && attInst.getAttribute().equals(attribute)) {
                    retorno.add(register);
                }
            });
        });
        return retorno;
    }

}

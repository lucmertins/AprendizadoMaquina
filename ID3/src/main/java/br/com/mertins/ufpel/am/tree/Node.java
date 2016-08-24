package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.id3.Gain;
import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Node implements Serializable {

    private final Attribute attribute;
    private final double gain;

    public Node(Attribute attribute, double gain) {
        this.attribute = attribute;
        this.gain = gain;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public double getGain() {
        return gain;
    }

    public void addEdge(List<Register> registers, List<Attribute> attributes) {
        attributes.remove(this.attribute);
        for (AttributeInstance attributeInstance : this.getAttribute().getAttributesInstance()) {
            List<Register> subconjunto = this.subconjunto(registers, this.getAttribute(), attributeInstance);
            Node node = null;
            double calcMax = 0;
            for (Attribute attributeTemp : attributes) {
                double calc = Gain.calc(subconjunto, attributeTemp);
                if (calc==0){
                    node= new Folha(attribute, gain);
                }
                if (node instanceof Folha && calcMax < calc) {
                    node = new Node(attributeTemp, calc);
                    calcMax = calc;
                }
            }
            if (node != null) {
                
            }
        }

    }

    @Override
    public String toString() {
        return String.format("Node {attribute= %s  gain=%f}", attribute, gain);
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

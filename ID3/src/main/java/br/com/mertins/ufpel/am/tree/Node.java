package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.id3.Gain;
import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
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
    private long positive;
    private long negative;
    private List<Edge> childrens = new ArrayList<>();

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

    public long getPositive() {
        return positive;
    }

    public void setPositive(long positive) {
        this.positive = positive;
    }

    public long getNegative() {
        return negative;
    }

    public void setNegative(long negative) {
        this.negative = negative;
    }

    public long totalRegisters() {
        return this.positive + this.negative;
    }

    public void addEdge(List<Register> registers, List<Attribute> attributes) {
        attributes.remove(this.attribute);
        if (!registers.isEmpty() && !attributes.isEmpty()) {
            for (AttributeInstance attributeInstance : this.getAttribute().getAttributesInstance()) {
                List<Register> subconjunto = this.subconjunto(registers, this.getAttribute(), attributeInstance);
                if (!subconjunto.isEmpty()) {
                    Node node = null;
                    double calcMax = 0;
                    for (Attribute attributeTemp : attributes) {
                        double calc = Gain.calc(subconjunto, attributeTemp);
                        if (calc == 0) {
                            long positivos = Gain.positivos(subconjunto, attributeInstance);
                            long negativos = Gain.negativos(subconjunto, attributeInstance);
                            node = new Leaf(attribute, positivos >= negativos ? Register.getLabelPositive(subconjunto) : Register.getLabelNegative(subconjunto));
                            node.setPositive(positive);
                            node.setNegative(negative);
                        }
                        if (!(node instanceof Leaf) && calcMax < calc) {
                            node = new Node(attributeTemp, calc);
                            calcMax = calc;
                        }
                    }
                    if (node != null) {
                        node.setPositive(Gain.positivos(subconjunto, attributeInstance));
                        node.setNegative(Gain.negativos(subconjunto, attributeInstance));
                        childrens.add(new Edge(attributeInstance, node));
                        if (!(node instanceof Leaf)) {
                            node.addEdge(subconjunto, attributes);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Node {attribute= %s  gain=%f positive=%d negative=%d}", attribute, gain, positive, negative);
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

    private class Edge implements Serializable {

        private final AttributeInstance attributeInstance;
        private final Node node;

        public Edge(AttributeInstance attributeInstance, Node node) {
            this.attributeInstance = attributeInstance;
            this.node = node;
        }

        public AttributeInstance getAttributeInstance() {
            return attributeInstance;
        }

        public Node getNode() {
            return node;
        }

        @Override
        public String toString() {
            return String.format("Edge {attributeInstance= %s  attribute=%s}", this.attributeInstance, this.node);
        }
    }
}

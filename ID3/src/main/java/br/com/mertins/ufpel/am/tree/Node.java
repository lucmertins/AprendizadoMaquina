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

    private Node parent;
    private final Attribute attribute;
    private final double gain;
    private long positive;
    private long negative;
    private AttributeInstance attributeInstanceParent;
    private final List<Edge> children = new ArrayList<>();

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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public AttributeInstance getAttributeInstanceParent() {
        return attributeInstanceParent;
    }

    public void setAttributeInstanceParent(AttributeInstance attributeInstanceParent) {
        this.attributeInstanceParent = attributeInstanceParent;
    }

    public long totalRegisters() {
        return this.positive + this.negative;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public void addChild(Leaf leaf) {
        children.add(new Edge(leaf.getAttributeInstanceParent(), leaf));
        leaf.setParent(this);
    }

    public void replace(Leaf leaf) {
        if (this.getParent() != null) {
            this.getParent().addChild(leaf);
            List<Edge> remover = new ArrayList<>();
            this.getParent().children.forEach(edge -> {
                if (edge.node.equals(this)) {
                    remover.add(edge);
                }
            });
            remover.forEach(edge -> {
                this.getParent().children.remove(edge);
            });
        }
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
                            node.setAttributeInstanceParent(attributeInstance);
                        }
                        if (!(node instanceof Leaf) && calcMax < calc) {
                            node = new Node(attributeTemp, calc);
                            node.setAttributeInstanceParent(attributeInstance);
                            calcMax = calc;
                        }
                    }
                    if (node != null) {
                        node.setPositive(Gain.positivos(subconjunto, attributeInstance));
                        node.setNegative(Gain.negativos(subconjunto, attributeInstance));
                        children.add(new Edge(attributeInstance, node));
                        node.setParent(this);
                        if (!(node instanceof Leaf)) {
                            node.addEdge(subconjunto, attributes);
                        }
                    }
                }
            }
        }
    }

    public List<Node> children() {
        List<Node> lista = new ArrayList<>();
        this.children.forEach(edge -> {
            lista.add(edge.getNode());
        });
        return lista;
    }

    @Override
    public String toString() {
        return String.format("Node %s gain=%f positive=%d negative=%d}", attribute, gain, positive, negative);
    }

    public StringBuilder print() {
        StringBuilder sb = new StringBuilder();
        print("", true, sb);
        return sb;
    }

    private void print(String prefix, boolean isTail, StringBuilder sb) {
        String value = this.getAttributeInstanceParent() == null ? "" : this.getAttributeInstanceParent().getValue();
        String text = this instanceof Leaf ? ((Leaf) this).getLabel().getValue() : this.getAttribute().getName();
        String proporcao = String.format("[%d+/%d-]", this.positive, this.negative);
        sb.append(String.format("%s%s(%s) %s  %s\n", prefix, (isTail ? "└── " : "├── "), value, text, proporcao));
        // System.out.printf("%s%s(%s) %s  %s\n", prefix, (isTail ? "└── " : "├── "), value, text, proporcao);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).node.print(prefix + (isTail ? "             " : "│            "), false, sb);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).node.print(prefix + (isTail ? "             " : "│            "), true, sb);
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

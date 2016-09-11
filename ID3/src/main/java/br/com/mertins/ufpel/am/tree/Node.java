package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.id3.Gain;
import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author mertins
 */
public class Node extends NodeBase {

    private ElementValue attribute;
    private final double gain;

    public Node(ElementValue attribute, double gain) {

        this.attribute = attribute;
        this.gain = gain;
    }

    public Attribute getAttribute() {
        return (Attribute) attribute;
    }

    public double getGain() {
        return gain;
    }

    public NodeBase returnChild(AttributeInstance attributeInstance) {
        for (Edge edge : this.childrenEdge) {
            if (edge.getAttributeInstance().equals(attributeInstance)) {
                return edge.getNode();
            }
        }
        return null;
    }

    public void replace(Leaf leaf) {
        if (this.getParent() != null) {
            this.getParent().addChild(this.getAttributeInstanceParent(), leaf);
            List<Edge> remover = new ArrayList<>();
            this.getParent().childrenEdge.forEach(edge -> {
                if (edge.getNode().equals(this)) {
                    remover.add(edge);
                }
            });
            remover.forEach(edge -> {
                this.getParent().childrenEdge.remove(edge);
            });
        }
    }

    public void addEdge(List<Register> registers, List<ElementValue> attributes) {
        attributes.remove(this.attribute);
        List<ElementValue> attributesTemp = new ArrayList<>(attributes);
        if (!registers.isEmpty() && !attributesTemp.isEmpty()) {
            for (AttributeInstance attributeInstance : this.getAttribute().getAttributesInstance()) {
                List<Register> subconjunto = this.subconjunto(registers, this.getAttribute(), attributeInstance);
                if (!subconjunto.isEmpty()) {
                    NodeBase node = null;
                    double calcMax = 0;
                    for (ElementValue attributeTemp : attributesTemp) {
                        double calc = Gain.calc(subconjunto, attributeTemp);
                        if (calc == 0) {
                            node = new Leaf(Register.dominant(subconjunto));
                            node.setAttributeInstanceParent(attributeInstance);
                        }
                        if (!(node instanceof Leaf) && calcMax < calc) {
                            node = new Node(attributeTemp, calc);
                            ((Node) node).setAttributeInstanceParent(attributeInstance);
                            calcMax = calc;
                        }
                    }
                    if (node != null) {
                        this.childrenEdge.add(new Edge(attributeInstance, node));
                        node.setParent(this);
                        node.add(subconjunto);
                        if (!(node instanceof Leaf)) {
                            ((Node) node).addEdge(subconjunto, attributesTemp);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Node %s gain=%f }", attribute, gain);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.attribute);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.attribute, other.attribute)) {
            return false;
        }
        if (this.getParent() != null && other.getParent() != null) {
            if (!Objects.equals(this.getParent().getAttributeInstanceParent(), other.getParent().getAttributeInstanceParent())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Node copy() {
        Node newNode = new Node(this.attribute.copy(), gain);
        super.copy(newNode);
        return newNode;

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

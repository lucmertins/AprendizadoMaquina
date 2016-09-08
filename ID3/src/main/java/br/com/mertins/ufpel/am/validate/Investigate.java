package br.com.mertins.ufpel.am.validate;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.NodeBase;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Investigate {

    private final Map<Label, Acumulador> acumulado;

    public Investigate() {
        this.acumulado = new HashMap<>();
        this.registers = null;
        this.root = null;
    }
    private final List<Register> registers;
    private final Node root;

    public Investigate(Node root, List<Register> registers, Set<Label> labels) {
        this.acumulado = new HashMap<>();
        this.registers = registers;
        this.root = root;
        labels.forEach(label -> {
            acumulado.put(label, new Acumulador());
        });
    }

    public Indicatives process() {

        this.registers.forEach(register -> {
            NodeBase node = root;
            if (!(node instanceof Leaf)) {
                for (AttributeInstance attributeInstance : register.getAttributesInstance()) {
                    if (node instanceof Node && ((Node) node).getAttribute() != null && ((Node) node).getAttribute().equals(attributeInstance.getAttribute())) {
                        NodeBase child = ((Node) node).returnChild(attributeInstance);
                        if (child instanceof Leaf) {
                            this.registra(((Leaf) child).getLabel(), register.getLabel());
                        } else {
                            node = child;
                        }
                    }
                }
            } else {
                this.registra(((Leaf) node).getLabel(), register.getLabel());
            }
        });
        Indicatives indicador = new Indicatives();
        acumulado.keySet().stream().forEach((label) -> {
            Acumulador acumulador = acumulado.get(label);
            indicador.add(label, acumulador.getTruePositive(), acumulador.getFalsePositive(), acumulador.getTrueNegative(), acumulador.getFalseNegative());
        });

        return indicador;
    }

    private void registra(Label labelTree, Label labelCorreto) {
        boolean acertou = labelTree.equals(labelCorreto);

        if (acertou) {
            acumulado.get(labelTree).addTruePositive();
            acumulado.keySet().stream().forEach(label -> {
                if (!labelTree.equals(label)) {
                    acumulado.get(label).addTrueNegative();
                }
            });
        } else {
            acumulado.get(labelTree).addFalsePositive();
            acumulado.get(labelCorreto).addFalseNegative();
        }

    }

    private class Acumulador {

        private long truePositive;
        private long falsePositive;
        private long trueNegative;
        private long falseNegative;

        public Acumulador() {
        }

        public long getTruePositive() {
            return truePositive;
        }

        public long getFalsePositive() {
            return falsePositive;
        }

        public long getTrueNegative() {
            return trueNegative;
        }

        public long getFalseNegative() {
            return falseNegative;
        }

        public void addTruePositive() {
            this.truePositive++;
        }

        public void addFalsePositive() {
            this.falsePositive++;
        }

        public void addTrueNegative() {
            this.trueNegative++;
        }

        public void addFalseNegative() {
            this.falseNegative++;
        }
    }
}

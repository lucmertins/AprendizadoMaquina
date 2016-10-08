package br.com.mertins.ufpel.am.validate;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.NodeBase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Investigate {

    private final Map<Label, Acumulador> acumulado;
    private final List<Register> registers;
    private final NodeBase root;

    public Investigate() {
        this.acumulado = new HashMap<>();
        this.registers = null;
        this.root = null;
    }

    public Investigate(NodeBase root, List<Register> registers, Set<Label> labels) {
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
        Indicatives indicador = new Indicatives(this.registers.size());
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
            acumulado.get(labelTree).addFalsePositive(labelCorreto);  // acrescentar o falso positivo informando o label correto
            acumulado.get(labelCorreto).addFalseNegative(labelTree);  // acrescentar o falso negativo informando qual o label que errou
        }

    }

    private class Acumulador {

        private long truePositive;
        private long trueNegative;
        private final Map<Label, Long> falsePositive = new HashMap<>();
        private final Map<Label, Long> falseNegative = new HashMap<>();

        public Acumulador() {
        }

        public long getTruePositive() {
            return truePositive;
        }

        public Map getFalsePositive() {
            return falsePositive;
        }

        public long getTrueNegative() {
            return trueNegative;
        }

        public Map getFalseNegative() {
            return falseNegative;
        }

        public void addTruePositive() {
            this.truePositive++;
        }

        public void addFalsePositive(Label label) {
            if (falsePositive.containsKey(label)) {
                falsePositive.put(label, falsePositive.get(label).longValue() + 1);
            } else {
                falsePositive.put(label, 1L);
            }
        }

        public void addTrueNegative() {
            this.trueNegative++;
        }

        public void addFalseNegative(Label label) {
            if (falseNegative.containsKey(label)) {
                falseNegative.put(label, falseNegative.get(label).longValue() + 1);
            } else {
                falseNegative.put(label, 1L);
            }
        }
    }
}

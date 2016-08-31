package br.com.mertins.ufpel.am.validate;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Investigate {

    private Indicativos indicativos;

    public Investigate() {
        this.registers = null;
        this.root = null;
    }
    private final List<Register> registers;
    private final Node root;

    public Investigate(List<Register> registers, Node root) {
        this.registers = registers;
        this.root = root;
    }

    public Indicativos getIndicativos() {
        return indicativos;
    }

    public void process() {
        this.indicativos = new Indicativos();
        this.registers.forEach(register -> {
            Node node = root;
            if (!(node instanceof Leaf)) {
                for (AttributeInstance attributeInstance : register.getAttributesInstance()) {
                    if (node.getAttribute().equals(attributeInstance.getAttribute())) {
                        Node child = node.returnChild(attributeInstance);
                        if (child instanceof Leaf) {
                            boolean acertou = ((Leaf) child).getLabel().equals(register.getLabel());
//                            System.out.printf("%s Linha %d    label %s    encontrou = %s\n", acertou ? "Acertou" : "Errou", register.getLine(), register.getLabel(), ((Leaf) child).getLabel());
                            this.registra(((Leaf) child).getLabel(), acertou);
                        } else {
                            node = child;
                        }
                    }
                }
            } else {
                boolean acertou = ((Leaf) node).getLabel().equals(register.getLabel());
//                System.out.printf("%s Linha %d    label %s    encontrou = %s\n", acertou ? "Acertou" : "Errou", register.getLine(), register.getLabel(), ((Leaf) node).getLabel());
                this.registra(((Leaf) node).getLabel(), acertou);
            }
        });
    }

    private void registra(Label label, boolean acertou) {
        if (acertou) {
            if (label.isPositive()) {
                this.indicativos.addVerdadeirosPositivos();
            } else {
                this.indicativos.addVerdadeirosNegativos();
            }
        } else if (label.isPositive()) {
            this.indicativos.addFalsosPositivos();
        } else {
            this.indicativos.addFalsosNegativos();
        }
    }
}

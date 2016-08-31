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

    private long verdadeirospositivos;
    private long verdadeirosnegativos;
    private long falsospositivos;
    private long falsosnegativos;
    private final List<Register> registers;
    private final Node root;

//    private final Set<Label> labels;
    public Investigate(List<Register> registers, Node root) {
        this.registers = registers;
        this.root = root;
    }
    
    public void process() {
        verdadeirospositivos=0;
        verdadeirosnegativos=0;
        falsospositivos=0;
        falsosnegativos=0;
        this.registers.forEach(register -> {
            Node node = root;
            if (node instanceof Leaf) {
                System.out.printf("Chegou na folha Label=%s    Register Label=%s\n", ((Leaf) node).getLabel().getValue(), register.getLabel().getValue());
            }
            for (AttributeInstance attributeInstance : register.getAttributesInstance()) {
                if (node.getAttribute().equals(attributeInstance.getAttribute())) {
                    Node child = node.returnChild(attributeInstance);
                    if (child instanceof Leaf) {
                        boolean acertou = ((Leaf) child).getLabel().equals(register.getLabel());
                        System.out.printf("%s Linha %d    label %s    encontrou = %s\n", acertou ? "Acertou" : "Errou", register.getLine(), register.getLabel(), ((Leaf) child).getLabel());
                        this.registra(((Leaf) child).getLabel(), acertou);
                    } else {
                        node = child;
                    }
                }
            }
        }
        );
        System.out.printf("vp %d    vn %d    fp %d    fn %d\n",verdadeirospositivos,verdadeirosnegativos,falsospositivos,falsosnegativos);
    }
    
    private void registra(Label label, boolean acertou) {
        if (acertou) {
            if (label.isPositive()) {
                verdadeirospositivos++;
            } else {
                verdadeirosnegativos++;
            }
        } else if (label.isPositive()) {
            falsospositivos++;
        } else {
            falsosnegativos++;
        }
    }
}

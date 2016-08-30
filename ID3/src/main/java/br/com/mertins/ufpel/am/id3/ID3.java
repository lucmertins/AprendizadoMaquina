package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
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

    public Node process() {
        Node root = null;
        double calcMax = 0;
        for (Attribute attribute : this.attributes) {
            double calc = Gain.calc(registers, attribute);
            if (calcMax < calc) {
                root = new Node(attribute, calc);
                calcMax = calc;
            }
        }
        if (root != null) {
            root.setPositive(Gain.positivos(registers));
            root.setNegative(Gain.negativos(registers));
            root.addEdge(registers, attributes);
        }
        avaliaRamoFolha(root);
        return root;
    }

    private void avaliaRamoFolha(Node node) {
        if (node != null && !(node instanceof Leaf)) {
            if (!node.hasChildren()) {
                Label label = getLabel(node.getPositive() - node.getNegative() >= 0);
                Leaf leaf = new Leaf(node.getAttribute(), label);
                leaf.setPositive(node.getPositive());
                leaf.setNegative(node.getNegative());
                leaf.setAttributeInstanceParent(node.getAttributeInstanceParent());
                node.replace(leaf);
            } else {
                List<Node> children = node.children();
                children.forEach(nodeChild -> {
                    avaliaRamoFolha(nodeChild);
                });
            }
        }
    }

    private Label getLabel(boolean positivo) {
        for (Label label : labels) {
            if (positivo && label.isPositive()) {
                return label;
            } else if (!positivo && !label.isPositive()) {
                return label;
            }
        }
        return null;
    }

}

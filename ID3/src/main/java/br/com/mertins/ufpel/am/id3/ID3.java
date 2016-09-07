package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.NodeBase;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class ID3 {

    private final List<Register> registers;
    private final List<ElementValue> attributes;
    private final Set<Label> labels;

    public ID3(List<Register> registers, List<ElementValue> attributes, Set<Label> labels) {
        this.registers = registers;
        this.attributes = attributes;
        this.labels = labels;
    }

    public Node process() {
        Node root = null;
        double calcMax = 0;
        for (ElementValue attribute : this.attributes) {
            double calc = Gain.calc(registers, attribute);
            if (calcMax < calc) {
                root = new Node(attribute, calc);
                calcMax = calc;
            }
        }
        if (root != null) {
            root.addEdge(registers, attributes);
        }
        avaliaRamoFolha(root);
        return root;
    }

    private void avaliaRamoFolha(NodeBase node) {
        if (node != null && !(node instanceof Leaf)) {
            if (!node.hasChildren()) {
                Label label = getLabel(node.getPositive() - node.getNegative() >= 0);
                Leaf leaf = new Leaf(label);
                leaf.setAttributeInstanceParent(((Node) node).getAttributeInstanceParent());
                ((Node) node).replace(leaf);
            } else {
                List<NodeBase> children = node.getChildren();
                children.forEach(nodeChild -> {
                    avaliaRamoFolha(nodeChild);
                });
            }
        }
    }

    private Label getLabel(boolean positivo) {
//        for (Label label : labels) {
//            if (positivo && label.isPositive()) {
//                return label;
//            } else if (!positivo && !label.isPositive()) {
//                return label;
//            }
//        }
        return null;
    }

}

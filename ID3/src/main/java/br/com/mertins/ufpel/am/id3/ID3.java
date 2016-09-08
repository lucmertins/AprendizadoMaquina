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
            root.add(registers);
            avaliaRamoFolha(root);
        }
        return root;
    }

    private void avaliaRamoFolha(NodeBase node) {
        if (node != null && !(node instanceof Leaf)) {
            if (!node.hasChildren()) {
                List<Register> subconjunto = Register.subconjunto(registers, ((Node)node.getParent()).getAttribute(), node.getAttributeInstanceParent());
                Leaf leaf = new Leaf(Register.dominant(subconjunto));
                leaf.setAttributeInstanceParent(((Node) node).getAttributeInstanceParent());
                leaf.add(subconjunto);
                ((Node) node).replace(leaf);
            } else {
                List<NodeBase> children = node.getChildren();
                children.forEach(nodeChild -> {
                    avaliaRamoFolha(nodeChild);
                });
            }
        }
    }
}

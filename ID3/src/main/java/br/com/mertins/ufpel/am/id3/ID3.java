package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Node;
import java.util.List;

/**
 *
 * @author mertins
 */
public class ID3 {

    private final List<Register> registers;
    private final List<Attribute> attributes;

    public ID3(List<Register> registers, List<Attribute> attributes) {
        this.registers = registers;
        this.attributes = attributes;
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
        return root;
    }

}

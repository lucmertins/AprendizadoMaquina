package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.NodeBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author mertins
 */
public class Rules {

    private final Map<Integer, Queue<NodeBase>> regras = new HashMap<>();

    private Rules() {
    }

    public Queue<NodeBase> getRole(Integer pos) {
        return regras.get(pos);
    }

    public int size() {
        return regras.size();
    }

    public void add(Queue<NodeBase> regra) {
        regras.put(regras.size() + 1, regra);
    }

    public List<Queue<NodeBase>> getRules() {
        List<Queue<NodeBase>> result = new ArrayList<>();
        for (int i = 1; i <= this.size(); i++) {
            result.add(this.getRole(i));
        }
        return result;
    }

    public static Rules instance(Node root) {
        Rules lista = new Rules();
        lista.process(root);
        return lista;
    }

    private void process(Node root) {
        Queue<NodeBase> lista = new LinkedList<>();
        this.geraRules(root, lista);
    }

    private void geraRules(NodeBase node, Queue<NodeBase> lista) {
        if (node instanceof Leaf) {
            Queue<NodeBase> novaLista = new LinkedList<>(lista);
            novaLista.add(node);
            this.add(novaLista);
        } else {
            Queue<NodeBase> novaLista = new LinkedList<>(lista);
            novaLista.add(node);
            node.getChildren().forEach(child -> {
                geraRules(child, novaLista);
            });
        }
    }

    public StringBuilder print() {
        StringBuilder sb = new StringBuilder();
        this.getRules().forEach(queue -> {
            queue.forEach(pop -> {
                if (pop.getParent() == null) {
                    if (pop instanceof Node) {
                        sb.append(String.format("%s ", ((Node) pop).getAttribute().getName()));
                    } else {
                        sb.append(String.format("%s ", ((Leaf) pop).getLabel().getValue()));
                    }
                } else if (pop instanceof Node) {
                    sb.append(String.format("(%s) -> %s ", pop.getAttributeInstanceParent().getValue(), ((Node) pop).getAttribute().getName()));
                } else {
                    sb.append(String.format("(%s) -> %s ", pop.getAttributeInstanceParent().getValue(), ((Leaf) pop).getLabel().getValue()));
                }
            });
            sb.append("\n");
        });
        return sb;
    }

}

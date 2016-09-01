package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.tree.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author mertins
 */
public class RuleList {

    private final Map<Integer, Queue<Node>> regras = new HashMap<>();

    public RuleList() {
    }

    public Queue<Node> getRole(Integer pos) {
        return regras.get(pos);
    }

    public int size() {
        return regras.size();
    }

    public void add(Queue<Node> regra) {
        regras.put(regras.size() + 1, regra);
    }

}

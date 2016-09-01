package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Leaf;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.validate.Investigate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author mertins
 */
public class PostPruning {

    private final Node root;
//    private List<Queue<Node>> regras;
    private RuleList regrasList = new RuleList();

    public PostPruning(Node root) {
        this.root = root;
    }

    public List<Queue<Node>> getRegras() {
        List<Queue<Node>> result = new ArrayList<>();
        for (int i = 1; i <= regrasList.size(); i++) {
            result.add(this.regrasList.getRole(i));
        }
        return result;
    }

    public void process(List<Register> registers) {
//        regras = new ArrayList<>();
        regrasList = new RuleList();
        Queue<Node> lista = new LinkedList<>();
        this.geraRules(this.root, lista);
//        for (Queue<Node> regra : this.regras) {
//            this.recreateBranch(regra);
//        }

        // gerar arvores com os nodos
        Investigate investigate = new Investigate(registers, root);
        investigate.process();

    }

    private void geraRules(Node node, Queue<Node> lista) {
        if (node instanceof Leaf) {
            Queue<Node> novaLista = new LinkedList<>(lista);
            novaLista.add(node);
            regrasList.add(novaLista);
        } else {
            Queue<Node> novaLista = new LinkedList<>(lista);
            novaLista.add(node);
            node.children().forEach(child -> {
                geraRules(child, novaLista);
            });
        }
    }

    private void recreateBranch(Queue<Node> regra) {
        Node root = null;

        for (Node node : regra) {

        }
    }

}

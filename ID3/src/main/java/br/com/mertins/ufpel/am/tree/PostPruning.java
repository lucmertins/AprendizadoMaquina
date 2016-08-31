package br.com.mertins.ufpel.am.tree;

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
    private List<Queue> regras;
//    Queue<Node> lista;

    public PostPruning(Node root) {
        this.root = root;
    }

    public List<Queue> getRegras() {
        return regras;
    }

    public void process() {
        regras = new ArrayList<>();
        Queue<Node> lista = new LinkedList<>();
        this.geraRoles(this.root, lista);
//        
        System.out.println("Feito");
    }

    private void geraRoles(Node node, Queue<Node> lista) {
        if (node instanceof Leaf) {
            Queue<Node> novaLista = new LinkedList<>(lista);
            novaLista.add(node);
            regras.add(novaLista);
        } else {
            Queue<Node> novaLista = new LinkedList<>(lista);
            novaLista.add(node);
            node.children().forEach(child -> {
                geraRoles(child, novaLista);
            });
        }
    }
}

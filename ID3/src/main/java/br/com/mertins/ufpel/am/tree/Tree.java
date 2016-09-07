package br.com.mertins.ufpel.am.tree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Tree {

    private final Node rootOrig;
    private Node rootTemp;
    private final List<Leaf> avaliadas = new ArrayList<>();

    public Tree(Node root) {
        this.rootOrig = root;
    }
    
    public Node pruning(){
        
        return rootOrig;
    }
    
//    private void geraRules(Node node, Queue<Node> lista) {
//        if (node instanceof Leaf) {
//            Queue<Node> novaLista = new LinkedList<>(lista);
//            novaLista.add(node);
//            regrasList.add(novaLista);
//        } else {
//            Queue<Node> novaLista = new LinkedList<>(lista);
//            novaLista.add(node);
//            node.children().forEach(child -> {
//                geraRules(child, novaLista);
//            });
//        }
//    }
}

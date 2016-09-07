package br.com.mertins.ufpel.am.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Tree {

    private final Node rootOrig;
    private final List<Leaf> avaliadas = new ArrayList<>();

    public Tree(Node root) {
        this.rootOrig = root;
    }

    public Node pruning() {
        Set<Leaf> findAllLeaf = this.findAllLeaf(rootOrig);

        findAllLeaf.forEach(leaf -> {
            System.out.printf("%s \n", leaf);
        });
        
        
        
        return rootOrig;
    }

    private Set<Leaf> findAllLeaf(Node node) {
        Set<Leaf> retorno = new HashSet<>();
        if (!(node instanceof Leaf)) {
            node.children().forEach(child -> {
                retorno.addAll(findAllLeaf(child));
            });
        } else {
            retorno.add((Leaf) node);
        }
        return retorno;
    }
}

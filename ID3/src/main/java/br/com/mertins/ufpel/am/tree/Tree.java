package br.com.mertins.ufpel.am.tree;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Tree {

    private final Node rootOrig;
    private Set<Leaf> valued = new HashSet<>();

    public Tree(Node root) {
        this.rootOrig = root;
    }

    public Set<Leaf> getValued() {
        return valued;
    }

    public void setValued(Set<Leaf> valued) {
        this.valued = valued;
    }

    public Node pruning(Set<Leaf> valued) {
        this.valued = valued;
        return this.pruning();
    }

    public Node pruning() {
        Set<Leaf> findAllLeaf = this.findAllLeaf(rootOrig);

        findAllLeaf.forEach(leaf -> {
            System.out.printf("%s \n", leaf);
        });

        return rootOrig;
    }

    private Set<Leaf> findAllLeaf(NodeBase node) {
        Set<Leaf> retorno = new HashSet<>();
        if (!(node instanceof Leaf)) {
            node.getChildren().forEach(child -> {
                retorno.addAll(findAllLeaf(child));
            });
        } else {
            retorno.add((Leaf) node);
        }
        return retorno;
    }
}

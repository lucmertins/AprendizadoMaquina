package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.Label;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        if (!findAllLeaf.isEmpty()) {
            Leaf leafCand = (Leaf) findAllLeaf.toArray()[0];
            this.valued.add(leafCand);
            if (leafCand.getParent() != null) {
                Node node = (Node) leafCand.getParent();
                List<NodeBase> children = node.getChildren();
                if (children != null) {
                    Map<Label, BigDecimal> novoSumario = new HashMap<>();
                    children.forEach(nodebase -> {
                        
                        Map<Label, BigDecimal> sumary = nodebase.sumary();
                        sumary.keySet().forEach(label -> {
//                            System.out.printf("\t\t label[%s]  %d\n", label, sumary.get(label).longValue());
                            if (novoSumario.containsKey(label)) {
                                novoSumario.put(label, novoSumario.get(label).add(sumary.get(label)));
                            } else {
                                novoSumario.put(label, sumary.get(label));
                            }
                        });
// totalizar o total de labels, escolhendo o que mais tem
                        System.out.printf("Sibling %s \n", nodebase);
                        novoSumario.keySet().forEach(label->{
                              System.out.printf("\t\t label[%s]  %d\n", label, sumary.get(label).longValue());
                        
                        });



                    });

                }
                System.out.printf("Tentar podar %s   pai %s\n", leafCand, leafCand.getParent());
            }
//            findAllLeaf.forEach(leaf -> {
//                System.out.printf("%s \n", leaf);
//            });
        }

        return rootOrig;
    }

    private Set<Leaf> findAllLeaf(NodeBase node) {
        Set<Leaf> retorno = new HashSet<>();
        if (!(node instanceof Leaf)) {
            node.getChildren().forEach(child -> {
                if (!this.valued.contains(child)) {
                    retorno.addAll(findAllLeaf(child));
                }
            });
        } else {
            retorno.add((Leaf) node);
        }
        return retorno;
    }
}

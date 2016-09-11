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

    private final NodeBase rootOrig;
    private Set<Leaf> valued = new HashSet<>();

    public Tree(NodeBase root) {
        this.rootOrig = root;
    }

    public Set<Leaf> getValued() {
        return valued;
    }

    public void setValued(Set<Leaf> valued) {
        this.valued = valued;
    }

    public NodeBase pruning(Set<Leaf> valued) {
        this.valued = valued;
        return this.pruning();
    }

    public NodeBase pruning() {
        Set<Leaf> findAllLeaf = this.findAllLeaf(rootOrig);
        if (!findAllLeaf.isEmpty()) {
            Leaf leafCand = (Leaf) findAllLeaf.toArray()[0];
            this.valued.add(leafCand);
            if (leafCand.getParent() != null) {
                Node node = (Node) leafCand.getParent();
                Map<Label, BigDecimal> sumarioAcumulado = this.sumarioAcumulado(rootOrig);
                Leaf leaf = new Leaf(this.bestLabel(sumarioAcumulado), sumarioAcumulado);
                if (node.getParent() == null) {
                    return leaf;
                } else {
                    node.replace(leaf);
                    return node.getParent();
                }
            }

        }
        return null;
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

    private Map<Label, BigDecimal> sumarioAcumulado(NodeBase node) {
        List<NodeBase> children = node.getChildren();
        Map<Label, BigDecimal> novoSumario = new HashMap<>();
        if (children != null) {
            children.forEach(nodebase -> {
                Map<Label, BigDecimal> sumary = nodebase.sumary();
                sumary.keySet().forEach(label -> {
                    if (novoSumario.containsKey(label)) {
                        novoSumario.put(label, novoSumario.get(label).add(sumary.get(label)));
                    } else {
                        novoSumario.put(label, sumary.get(label));
                    }
                });
            });
        }
        return novoSumario;
    }

    private Label bestLabel(Map<Label, BigDecimal> sumarioAcumulado) {
        Label label = null;
        long valueLabel = 0L;
        for (Label labelTemp : sumarioAcumulado.keySet()) {
            if (label == null || sumarioAcumulado.get(labelTemp).longValue() > valueLabel) {
                label = labelTemp;
                valueLabel = sumarioAcumulado.get(label).longValue();
            }
        }
        return label;
    }
}

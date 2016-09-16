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

    private final NodeBase nodeRoot;
    private final NodeBase nodeOrigin;
    private Set<Leaf> pruned = new HashSet<>();

    public Tree(NodeBase root) {
        this.nodeRoot = NodeBase.copyNode(root);
        this.nodeOrigin = NodeBase.copyNode(root);
    }

    public NodeBase getNodeRoot() {
        return nodeRoot;
    }

    public Set<Leaf> getPruned() {
        return pruned;
    }

    public void setPruned(Set<Leaf> pruned) {
        this.pruned = pruned;
    }

    public NodeBase pruning(Set<Leaf> valued) {
        this.pruned = valued;
        return this.pruning();
    }

    public NodeBase pruning() {
        Set<Leaf> findAllLeaf = this.findAllLeaf(nodeRoot);
        if (!findAllLeaf.isEmpty()) {
            Leaf leafCand = (Leaf) findAllLeaf.toArray()[0];
            this.pruned.add(leafCand);
            if (leafCand.getParent() != null) {
                Node node = (Node) leafCand.getParent();
                if (node.getParent() == null) {
                    Map<Label, BigDecimal> sumarioAcumulado = this.sumarioAcumulado(nodeRoot);
                    Leaf leaf = new Leaf(this.bestLabel(sumarioAcumulado), sumarioAcumulado);
                    return leaf;
                } else {
                    Map<Label, BigDecimal> sumarioAcumulado = this.sumarioAcumulado(node);
                    Leaf leaf = new Leaf(this.bestLabel(sumarioAcumulado), sumarioAcumulado);
                    leaf.setAttributeInstanceParent(node.getAttributeInstanceParent());
                    node.replace(leaf);
                    return nodeRoot;
                }
            }
        }
        return null;
    }

    /**
     * Retorna a árvore original, indicando quais folhas foram podadas.
     *
     * @return
     */
    public Tree origin() {
        Tree tree = new Tree(nodeOrigin);
        tree.setPruned(pruned);
        return tree;
    }

    private Set<Leaf> findAllLeaf(NodeBase node) {
        Set<Leaf> retorno = new HashSet<>();
        if (!(node instanceof Leaf)) {
            node.getChildren().forEach(child -> {
                if (child instanceof Leaf) {
                    if (!this.pruned.contains(child)) {
                        retorno.addAll(findAllLeaf(child));
                    }
                } else {
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

package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mertins
 */
public abstract class NodeBase implements Serializable {

    private NodeBase parent;
    private AttributeInstance attributeInstanceParent;
    protected List<NodeBase.Edge> childrenEdge = new ArrayList<>();
    protected Map<Label, BigDecimal> sumary = new HashMap<>();

    public NodeBase() {
    }

    public NodeBase getParent() {
        return parent;
    }

    public void setParent(NodeBase parent) {
        this.parent = parent;
    }

    public AttributeInstance getAttributeInstanceParent() {
        return attributeInstanceParent;
    }

    public void setAttributeInstanceParent(AttributeInstance attributeInstanceParent) {
        this.attributeInstanceParent = attributeInstanceParent;
    }

    public StringBuilder print() {
        StringBuilder sb = new StringBuilder();
        print("", true, sb);
        return sb;
    }

    public boolean hasChildren() {
        return !this.childrenEdge.isEmpty();
    }

    public void addChild(AttributeInstance attributeInstance, NodeBase leaf) {
        childrenEdge.add(new Edge(attributeInstance, leaf));
        leaf.setParent(this);
    }

    public List<NodeBase> getChildren() {
        List<NodeBase> lista = new ArrayList<>();
        this.childrenEdge.forEach(edge -> {
            lista.add(edge.getNode());
        }
        );
        return lista;
    }

    public void add(List<Register> registers) {
        registers.stream().forEach((register) -> {
            if (sumary.containsKey(register.getLabel())) {
                sumary.put(register.getLabel(), sumary.get(register.getLabel()).add(BigDecimal.ONE));
                sumary.get(register.getLabel());
            } else {
                sumary.put(register.getLabel(), BigDecimal.ONE);
            }
        });
    }

    public Map<Label, BigDecimal> sumary() {
        return this.sumary;
    }

    protected NodeBase copy(NodeBase newNodeBase) {
        newNodeBase.parent = this.parent;
        newNodeBase.attributeInstanceParent = this.attributeInstanceParent;
        newNodeBase.sumary = new HashMap<>(this.sumary);
        return newNodeBase;
    }

    protected abstract NodeBase copy();

    public static NodeBase copyNode(NodeBase nodeBase) {
        NodeBase copy = nodeBase.copy();
        NodeBase.copyNode(nodeBase, copy);
        return copy;
    }

    static void copyNode(NodeBase nodeBase, NodeBase nodeBaseCopy) {
        if (!(nodeBase instanceof Leaf)) {
            nodeBase.childrenEdge.forEach(edge -> {
                NodeBase nodeChildrenCopy = edge.getNode().copy();
                nodeBaseCopy.AddEdge(edge.getAttributeInstance(), nodeChildrenCopy);
                nodeChildrenCopy.setParent(nodeBaseCopy);
                Node.copyNode(edge.getNode(), nodeChildrenCopy);
            });
        }
    }

    void AddEdge(AttributeInstance attributeInstance, NodeBase node) {
        this.childrenEdge.add(new Edge(attributeInstance, node));

    }

    private void print(String prefix, boolean isTail, StringBuilder sb) {
        String value = this.getAttributeInstanceParent() == null ? "" : this.getAttributeInstanceParent().getValue();
        String text = this instanceof Leaf ? ((Leaf) this).getLabel().getValue() : ((Node) this).getAttribute().getName();
        StringBuilder sbTemp = new StringBuilder();
        this.sumary.keySet().stream().map((label) -> {
            return label;
        }).forEach((label) -> {
            sbTemp.append(String.format("%s/%d ", label.getValue(), this.sumary.get(label).intValue()));
        });
        if (sbTemp.length() > 0) {
            sbTemp.deleteCharAt(sbTemp.length() - 1);
        }
        sb.append(String.format("%s%s(%s) %s  [%s]\n", prefix, (isTail ? "└── " : "├── "), value, text, sbTemp.toString()));
        for (int i = 0; i < childrenEdge.size() - 1; i++) {
            childrenEdge.get(i).node.print(prefix + (isTail ? "             " : "│            "), false, sb);
        }
        if (childrenEdge.size() > 0) {
            childrenEdge.get(childrenEdge.size() - 1).node.print(prefix + (isTail ? "             " : "│            "), true, sb);
        }
    }

    protected class Edge implements Serializable {

        private final AttributeInstance attributeInstance;
        private final NodeBase node;

        public Edge(AttributeInstance attributeInstance, NodeBase node) {
            this.attributeInstance = attributeInstance;
            this.node = node;
        }

        public AttributeInstance getAttributeInstance() {
            return attributeInstance;
        }

        public NodeBase getNode() {
            return node;
        }

        @Override
        public String toString() {
            return String.format("Edge {attributeInstance= %s  attribute=%s}", this.attributeInstance, this.node);
        }
    }
}

package br.com.mertins.ufpel.am.tree;

import br.com.mertins.ufpel.am.preparacao.AttributeInstance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class NodeBase implements Serializable {

    private NodeBase parent;

    protected AttributeInstance attributeInstanceParent;

    public void setAttributeInstanceParent(AttributeInstance attributeInstanceParent) {
        this.attributeInstanceParent = attributeInstanceParent;
    }
    private long positive;
    private long negative;
    protected final List<NodeBase.Edge> childrenEdge = new ArrayList<>();

    public long getPositive() {
        return positive;
    }

    public void setPositive(long positive) {
        this.positive = positive;
    }

    public long getNegative() {
        return negative;
    }

    public void setNegative(long negative) {
        this.negative = negative;
    }

    public NodeBase getParent() {
        return parent;
    }

    public void setParent(NodeBase parent) {
        this.parent = parent;
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

    private void print(String prefix, boolean isTail, StringBuilder sb) {
        String value = this instanceof Node ? ((Node) this).getAttributeInstanceParent() == null ? "" : ((Node) this).getAttributeInstanceParent().getValue() : "";
        String text = this instanceof Leaf ? ((Leaf) this).getLabel().getValue() : ((Node) this).getAttribute().getName();
        String proporcao = String.format("[%d+/%d-]", this.getPositive(), this.getNegative());
        sb.append(String.format("%s%s(%s) %s  %s\n", prefix, (isTail ? "└── " : "├── "), value, text, proporcao));
        // System.out.printf("%s%s(%s) %s  %s\n", prefix, (isTail ? "└── " : "├── "), value, text, proporcao);
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

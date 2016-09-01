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
    private List<Queue> regras;

    public PostPruning(Node root) {
        this.root = root;
    }

    public List<Queue> getRegras() {
        return regras;
    }

    public void process(List<Register> registers) {
        regras = new ArrayList<>();
        Queue<Node> lista = new LinkedList<>();
        this.geraRoles(this.root, lista);
        
        // gerar arvores com os nodos
        
        
        
        Investigate investigate = new Investigate(registers, root);
        investigate.process();
        
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
    
    private void recreateRamo(Queue<Node> regra){
        
        regra.forEach(node->{
            
        });
        
    }
}

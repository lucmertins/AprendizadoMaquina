package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Label;
import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.NodeBase;
import br.com.mertins.ufpel.am.tree.Tree;
import br.com.mertins.ufpel.am.validate.Indicatives;
import br.com.mertins.ufpel.am.validate.Investigate;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class PostPruning {

    private final NodeBase root;

    public PostPruning(NodeBase root) {
        this.root = root;
    }

    public NodeBase process(List<Register> registers, Set<Label> labels) {
        Investigate investigate = new Investigate(root, registers, labels);
        Indicatives indicativos = investigate.process();
        NodeBase bestTree = root;

        Tree tree = new Tree(root);
        NodeBase treeAval = tree.pruning();
        if (treeAval != null) {
            Investigate investigateAval = new Investigate(treeAval, registers, labels);
            Indicatives indicativosAval = investigateAval.process();
            System.out.printf("***arvore podada \n%s\n***\n", treeAval.print());
            if (indicativos.accuracy().compareTo(indicativosAval.accuracy()) > 0) {
                System.out.printf("Poda piorou a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), indicativosAval.accuracy().doubleValue());
            } else {
                bestTree = treeAval;
                System.out.printf("Poda melhorou ou deixou igual a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), indicativosAval.accuracy().doubleValue());
            }
        }
        return bestTree;
    }

}

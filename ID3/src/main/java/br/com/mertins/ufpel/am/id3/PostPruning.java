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

    private final Tree treeRoot;

    public PostPruning(NodeBase root) {
        this.treeRoot = new Tree(root);
    }

    public NodeBase process(List<Register> registers, Set<Label> labels) {
        Investigate investigate = new Investigate(treeRoot.getNodeRoot(), registers, labels);
        Indicatives indicativos = investigate.process();
        NodeBase bestRoot = treeRoot.getNodeRoot();

        Tree tree = new Tree(bestRoot);
        NodeBase nodeAval;
        while ((nodeAval = tree.pruning()) != null) {
            Investigate investigateAval = new Investigate(nodeAval, registers, labels);
            Indicatives indicativosAval = investigateAval.process();
//            System.out.printf("***arvore podada \n%s\n***\n", nodeAval.print());
            if (indicativos.accuracy().compareTo(indicativosAval.accuracy()) > 0) {
                tree = tree.origin();
//                System.out.printf("Poda piorou a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), indicativosAval.accuracy().doubleValue());
            } else {
                bestRoot = nodeAval;
//                System.out.printf("Poda melhorou ou deixou igual a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), indicativosAval.accuracy().doubleValue());
            }
        }

        return bestRoot;
    }

}

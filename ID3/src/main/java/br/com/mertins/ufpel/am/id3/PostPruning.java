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

    private final NodeBase rootCopy;
    private StringBuilder logPrunning;

    public PostPruning(NodeBase root) {
        rootCopy = NodeBase.copyNode(root);

    }

    public StringBuilder getLogPrunning() {
        return logPrunning;
    }

    public NodeBase process(List<Register> registers, Set<Label> labels) {
        Investigate investigate = new Investigate(rootCopy, registers, labels);
        Indicatives indicativos = investigate.process();
        NodeBase bestRoot = rootCopy;
        Tree tree = new Tree(rootCopy);
        logPrunning = new StringBuilder();
        NodeBase nodeAval;
        while ((nodeAval = tree.pruning()) != null) {
            Investigate investigateAval = new Investigate(nodeAval, registers, labels);
            Indicatives indicativosAval = investigateAval.process();
            logPrunning.append(String.format("Arvore podada \n%s\n***\n", nodeAval.print()));
            if (indicativos.accuracy().compareTo(indicativosAval.accuracy()) > 0) {
                tree = tree.origin();
                logPrunning.append(String.format("Poda piorou a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), 
                        indicativosAval.accuracy().doubleValue()));
                logPrunning.append(String.format("Voltar para arvore anterior\n %s", tree.getNodeRoot().print()));
            } else {
                bestRoot = nodeAval;
                tree = new Tree(bestRoot);
                logPrunning.append(String.format("Poda melhorou ou deixou igual a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), 
                        indicativosAval.accuracy().doubleValue()));
            }
        }
        return bestRoot;
    }

}

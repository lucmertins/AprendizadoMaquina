package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Register;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.Tree;
import br.com.mertins.ufpel.am.validate.Indicatives;
import br.com.mertins.ufpel.am.validate.Investigate;
import java.util.List;

/**
 *
 * @author mertins
 */
public class PostPruning {

    private final Node root;

    public PostPruning(Node root) {
        this.root = root;
    }

    public void process(List<Register> registers) {
        Investigate investigate = new Investigate(registers, root);
        Indicatives indicativos = investigate.process();
        Tree tree = new Tree(root);
        Node treeAval = tree.pruning();
        Investigate investigateAval = new Investigate(registers, treeAval);
        Indicatives indicativosAval = investigateAval.process();
        if (indicativos.accuracy().compareTo(indicativosAval.accuracy()) > 0) {
            System.out.printf("Poda piorou a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), indicativosAval.accuracy().doubleValue());
        } else {
            System.out.printf("Poda melhorou ou deixou igual a acuracia     Sem Poda [%f]    Com Poda [%f]\n ", indicativos.accuracy().doubleValue(), indicativosAval.accuracy().doubleValue());
        }
    }

}

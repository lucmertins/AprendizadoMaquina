package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Sample;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.NodeBase;
import br.com.mertins.ufpel.am.validate.Indicatives;
import br.com.mertins.ufpel.am.validate.Investigate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void main(String[] args) {
//        String fileName = "beach.csv";
        String fileName = "beach_Ruido.csv";
        if (args.length == 1) {
            fileName = args[0];
        }
        try {
            System.out.println("****** Preparação");
            File file = new File(fileName);
            System.out.printf("Arquivo %s\n", file.getAbsolutePath());
            Sample sample = new Sample();
            sample.avaliaFirstLine(file);
            System.out.println("****** Ajuste");
            List<Integer> remove = new ArrayList<>();
            remove.add(0);
//            remove.add(1);
            sample.removeAttributesPos(remove);
            sample.defineColumnLabel(5);
            sample.process(file);
            System.out.println("******ID3");
            ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes(), sample.getLabels());
            Node root = id3.process();
            System.out.println(root.print());
            System.out.println("****** Testa ID3");
            Investigate investigate = new Investigate(root, sample.getRegisters(), sample.getLabels());
            Indicatives indicativo = investigate.process();
            sample.getLabels().forEach(label -> {
                System.out.printf("Label [%s]\n", label.getValue());
                System.out.printf("  VP %d   FP %d   VN %d   FN %d\n", indicativo.getTruePositives(label).intValue(),
                        indicativo.getFalsePositives(label).intValue(),
                        indicativo.getTrueNegatives(label).intValue(), indicativo.getFalseNegatives(label).intValue());
                System.out.printf("  Precisão %f    Recall %f    F1 %f\n", indicativo.precision(label).doubleValue(),
                        indicativo.recall(label).doubleValue(), indicativo.f1(label).doubleValue());

            });
            System.out.printf("\nAcurácia %f\n", indicativo.accuracy().doubleValue());

//            System.out.printf("VP %d   FP %d   VN %d   FN %d\n", indicativos.getVerdadeirosPositivos().intValue(), indicativos.getFalsosPositivos().intValue(),
//                    indicativos.getVerdadeirosNegativos().intValue(), indicativos.getFalsosNegativos().intValue());
//            System.out.printf("Acurácia %f       Precisão %f    Recall %f    F1 %f\n", indicativos.accuracy().doubleValue(), indicativos.precision().doubleValue(), indicativos.recall().doubleValue(), indicativos.f1().doubleValue());
//            System.out.println("Matriz de Confusão");
//            System.out.printf("\t%s\t\t%s\n", lbPositive != null ? lbPositive.getValue() : "?", lbNegative != null ? lbNegative.getValue() : "?");
//            System.out.printf("%s\t%d\t\t%d\n", lbPositive != null ? lbPositive.getValue() : "?", indicativos.getVerdadeirosPositivos().intValue(), indicativos.getFalsosPositivos().intValue());
//            System.out.printf("%s\t%d\t\t%d\n", lbNegative != null ? lbNegative.getValue() : "?", indicativos.getFalsosNegativos().intValue(), indicativos.getVerdadeirosNegativos().intValue());
            System.out.println("***** Regras");
            Rules rules = Rules.instance(root);
            System.out.println(rules.print());
            System.out.println("***** Poda");
//
            PostPruning pruning = new PostPruning(root);
            NodeBase bestTree = pruning.process(sample.getRegisters(), sample.getLabels());

            System.out.println("***** Best tree");
            System.out.println(bestTree.print());
            System.out.println("*****");

             System.out.println("***** Origin tree");
            System.out.println(root.print());
            System.out.println("*****");
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }
}

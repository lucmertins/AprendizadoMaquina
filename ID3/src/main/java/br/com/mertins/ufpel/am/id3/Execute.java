package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Sample;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.validate.Indicatives;
import br.com.mertins.ufpel.am.validate.Investigate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            try (FileReader arq = new FileReader(fileName)) {
                BufferedReader lerArq = new BufferedReader(arq);
                sample.avaliaFirstLine(lerArq);
            }
            System.out.println("****** Ajuste");
            List<Integer> remove = new ArrayList<>();
            remove.add(0);
//            remove.add(1);
            sample.removeAttributesPos(remove);
            sample.defineColumnLabel(5);
            try (FileReader arq = new FileReader(fileName)) {
                BufferedReader lerArq = new BufferedReader(arq);
                sample.process(lerArq);
            }

            System.out.println("******ID3");
            ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes(), sample.getLabels());
            Node root = id3.process();
            StringBuilder print = root.print();
            System.out.println(print.toString());
            System.out.println("****** Testa ID3");
            Investigate investigate = new Investigate(root, sample.getRegisters(), sample.getLabels());
            Set<Indicatives> indicativos = investigate.process();
            indicativos.forEach(indicativo -> {
                System.out.printf("Label [%s]\n", indicativo.getLabel().getValue());
                System.out.printf("VP %d   FP %d   VN %d   FN %d\n", indicativo.getTruePositives().intValue(), indicativo.getFalsePositives().intValue(),
                        indicativo.getTrueNegatives().intValue(), indicativo.getFalseNegatives().intValue());
                System.out.printf("Acurácia %f       Precisão %f    Recall %f    F1 %f\n", indicativo.accuracy().doubleValue(), indicativo.precision().doubleValue(), indicativo.recall().doubleValue(), indicativo.f1().doubleValue());

            });

//            System.out.printf("VP %d   FP %d   VN %d   FN %d\n", indicativos.getVerdadeirosPositivos().intValue(), indicativos.getFalsosPositivos().intValue(),
//                    indicativos.getVerdadeirosNegativos().intValue(), indicativos.getFalsosNegativos().intValue());
//            System.out.printf("Acurácia %f       Precisão %f    Recall %f    F1 %f\n", indicativos.accuracy().doubleValue(), indicativos.precision().doubleValue(), indicativos.recall().doubleValue(), indicativos.f1().doubleValue());
//            System.out.println("Matriz de Confusão");
//            System.out.printf("\t%s\t\t%s\n", lbPositive != null ? lbPositive.getValue() : "?", lbNegative != null ? lbNegative.getValue() : "?");
//            System.out.printf("%s\t%d\t\t%d\n", lbPositive != null ? lbPositive.getValue() : "?", indicativos.getVerdadeirosPositivos().intValue(), indicativos.getFalsosPositivos().intValue());
//            System.out.printf("%s\t%d\t\t%d\n", lbNegative != null ? lbNegative.getValue() : "?", indicativos.getFalsosNegativos().intValue(), indicativos.getVerdadeirosNegativos().intValue());
            System.out.println("*****");
//            Rules rules = Rules.instance(root, sample.getRegisters());
//            List<Queue<NodeBase>> regras = rules.getRules();
//            regras.forEach((Queue rule) -> {
//                while (!rule.isEmpty()) {
//                    NodeBase pop = (NodeBase) rule.poll();
//                    System.out.printf("%s ", pop instanceof Leaf ? String.format("%s", ((Leaf) pop).getLabel().getValue()) : String.format("(%s) %s", ((Node) pop).getAttributeInstanceParent() != null ? ((Node) pop).getAttributeInstanceParent().getValue() : "", ((Node) pop).getAttribute().getName()));
//                }
//                System.out.println();
//            });
//            System.out.println("*****");
//
//            PostPruning pruning = new PostPruning(root);
//            pruning.process(sample.getRegisters());
//
//            System.out.println("*****");

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }
}

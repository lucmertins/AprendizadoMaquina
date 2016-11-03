package br.com.mertins.ufpel.avaliacao.redeneural;

import java.io.PrintStream;

/**
 *
 * @author mertins
 */
public class ConfusionMatrix {

    public void matrix(Accumulator[] accumulators, PrintStream out) {
        // Linha dos labels reconhecidos
        for (int i = 0; i < 10; i++) {
            out.printf("\t%d\t", i);
        }
        out.println();
        // Coluna dos labels corretos
        for (int y = 0; y < 10; y++) {
            out.printf("%d\t", y);
            for (int x = 0; x < 10; x++) {
                if (x == y) {
                    out.printf("%.0f\t\t", accumulators[x].getTruePositive());
                } else {
                    out.printf("\t\t");
                }
            }
            out.println();
        }

    }

    public void resumo(Accumulator[] accumulators, PrintStream out) {
        for (int i = 0; i < 10; i++) {
            out.printf("***** Label  %d\n", i);
            Accumulator accumulator = accumulators[i];
            out.printf("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f] \n", accumulator.getTruePositive(), accumulator.getTrueNegative(), accumulator.totalFalsePositive(), accumulator.totalFalseNegative());
            for (Integer key : accumulator.getFalsePositive().keySet()) {
                out.printf("FalsePositivo achou que %d era %d [%.0f vezes] \n", key, i, accumulator.getFalsePositive().get(key));
            }
            for (Integer key : accumulator.getFalseNegative().keySet()) {
                out.printf("FalseNegative era %d acusou %d [%.0f vezes] \n", key, i, accumulator.getFalseNegative().get(key));
            }
            out.printf("Acuracia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n", accumulator.accuracy(), accumulator.precision(), accumulator.recall(), accumulator.f1());
        }
    }
}

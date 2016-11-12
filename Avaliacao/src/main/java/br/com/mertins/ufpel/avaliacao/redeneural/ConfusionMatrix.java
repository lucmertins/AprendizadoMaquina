package br.com.mertins.ufpel.avaliacao.redeneural;

import java.io.PrintStream;
import java.util.Map;

/**
 *
 * @author mertins
 */
public class ConfusionMatrix {

    public void matrix(Accumulator[] accumulators, PrintStream out) {
        // Linha dos labels reconhecidos
        for (int i = 0; i < 10; i++) {
            out.printf("\t%d", i);
        }
        out.println();
        out.println();
        // Coluna dos labels corretos
        for (int y = 0; y < 10; y++) {
            out.printf("%d\t", y);
            for (int x = 0; x < 10; x++) {
                if (x == y) {
                    out.printf("%.0f\t", accumulators[x].getTruePositive());
                } else {
                    Map<Integer, Double> falseNegatives = accumulators[x].getFalseNegative();
                    Double value = falseNegatives.get(y);
                    out.printf("%.0f\t", value == null ? 0 : value);
                }
            }
            out.println();
        }

    }

    public void detalhamento(Accumulator[] accumulators, PrintStream out) {
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

    public void resumo(Accumulator[] accumulators, PrintStream out) {
        for (int i = 0; i < 10; i++) {
            out.printf("***** Label  %d\n", i);
            Accumulator accumulator = accumulators[i];
            out.printf("Acuracia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n", accumulator.accuracy(), accumulator.precision(), accumulator.recall(), accumulator.f1());
        }
    }

    public double accuracy(Accumulator[] accumulators) {
        Totais tot = new Totais();
        tot.sum(accumulators);
        return (tot.truePositive + tot.trueNegative) / (tot.truePositive + tot.trueNegative + tot.falsePositive + tot.falseNegative);
    }

    public double precision(Accumulator[] accumulators) {
        Totais tot = new Totais();
        tot.sum(accumulators);
        return tot.truePositive / (tot.truePositive + tot.falsePositive);
    }

    public double recall(Accumulator[] accumulators) {
        Totais tot = new Totais();
        tot.sum(accumulators);
        return tot.truePositive / (tot.truePositive + tot.falseNegative);
    }

    public double f1(Accumulator[] accumulators) {
        double prec = this.precision(accumulators);
        double rec = this.recall(accumulators);
        return 2 * (prec * rec / (prec + rec));
    }

    private class Totais {

        private double truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;

        public void sum(Accumulator[] accumulators) {
            for (Accumulator acum : accumulators) {
                truePositive += acum.getTruePositive();
                trueNegative += acum.getTrueNegative();
                falsePositive += acum.totalFalsePositive();
                falseNegative += acum.totalFalseNegative();
            }
//            System.out.printf("\nTP %.12f       TN %.12f     FP %.12f     FN %.12f\n",truePositive,trueNegative,falsePositive,falseNegative);
        }

    }
}

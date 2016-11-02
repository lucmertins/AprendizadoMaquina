package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.OutPerceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mertins
 */
public class ExecuteAvaliacao {

    private final FileWriter outFile;

    public ExecuteAvaliacao(FileWriter outFile) {
        this.outFile = outFile;
    }

    public void run(File fileTest, SamplesParameters samplesParameters, String fileMLP) throws IOException, ClassNotFoundException {
        Samples samples = new Samples(samplesParameters, new FunctionSampleOut() {
            @Override
            public void prepare(String value, Sample sample) {
                for (int i = 0; i < 10; i++) {
                    sample.addOut(Integer.valueOf(value) == i ? 1 : 0);
                }
                sample.addOutOriginal(value);
            }
        });
        samples.avaliaFirstLine(fileTest);
        samples.open(fileTest);
        Sample sample;
        MLP rede = MLP.deserialize(fileMLP);
        Acumulador[] acumuladores = new Acumulador[10];
        for (int i = 0; i < 10; i++) {
            acumuladores[i] = new Acumulador(i);
        }
        int pos = 0;
        while ((sample = samples.next()) != null) {
//            if (pos++ > 20) {
//                break;
//            }
            rede.updateIn(sample);
            OutPerceptron[] outs = rede.process();

            int saidaEscolhida = -1;
            double melhorValor = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < outs.length; i++) {
                if (outs[i].getOut() > melhorValor) {
                    melhorValor = outs[i].getOut();
                    saidaEscolhida = i;
                }
            }
            int saidaCorreta = Integer.parseInt(sample.getOutOriginal(1));
            if (saidaEscolhida == saidaCorreta) {
                // verdadeiro positivo para saidaescolhida
//                System.out.printf("True Positive Sample [%d]   out [%d]\n", saidaCorreta, saidaEscolhida);
                acumuladores[saidaEscolhida].addTruePositive();
                // verdadeiro negativo para todos os outros labels
                for (int i = 0; i < 10; i++) {
                    if (i != saidaEscolhida) {
                        acumuladores[i].addTrueNegative();
                    }
                }
            } else {
//                System.out.printf("False Positive Sample [%d]   out [%d]\n", saidaCorreta, saidaEscolhida);
                // falso positivo para o saidaEscolhida
                acumuladores[saidaEscolhida].addFalsePositive(saidaCorreta);

                // falso negativo para o valor original
                acumuladores[saidaCorreta].addFalseNegative(saidaEscolhida);

                // verdadeiro negativo para todos os outros labels 
                for (int i = 0; i < 10; i++) {
                    if (i != saidaEscolhida && i != saidaCorreta) {
                        acumuladores[i].addTrueNegative();
                    }
                }

            }
        }
        samples.close();
        for (int i = 0; i < 10; i++) {
            System.out.printf("***** Label  %d\n", i);
            Acumulador acumulador = acumuladores[i];
            if (outFile != null) {
                outFile.write(String.format("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(), acumulador.totalFalseNegative()));
                for (Integer key : acumulador.getFalsePositive().keySet()) {
                    outFile.write(String.format("FalsePositivo achou que %d era %d [%.0f vezes] \n", key, i, acumulador.getFalsePositive().get(key)));
                }
                for (Integer key : acumulador.getFalseNegative().keySet()) {
                    outFile.write(String.format("FalseNegative era %d acusou %d [%.0f vezes]  \n", key, i, acumulador.getFalseNegative().get(key)));
                }
                outFile.write(String.format("Acuracia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n", acumulador.accuracy(), acumulador.precision(), acumulador.recall(), acumulador.f1()));
            } else {
                System.out.printf("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f] \n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(), acumulador.totalFalseNegative());
                for (Integer key : acumulador.getFalsePositive().keySet()) {
                    System.out.printf("FalsePositivo achou que %d era %d [%.0f vezes] \n", key, i, acumulador.getFalsePositive().get(key));
                }
                for (Integer key : acumulador.getFalseNegative().keySet()) {
                    System.out.printf("FalseNegative era %d acusou %d [%.0f vezes] \n", key, i, acumulador.getFalseNegative().get(key));
                }
                System.out.printf("Acuracia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n", acumulador.accuracy(), acumulador.precision(), acumulador.recall(), acumulador.f1());
            }
        }
    }

    private class Acumulador {

        private final int label;
        private double truePositive;
        private double trueNegative;
        private final Map<Integer, Double> falsePositive = new HashMap<>();
        private final Map<Integer, Double> falseNegative = new HashMap<>();

        public Acumulador(int label) {
            this.label = label;
        }

        public int getLabel() {
            return label;
        }

        public double getTruePositive() {
            return truePositive;
        }

        public Map<Integer, Double> getFalsePositive() {
            return falsePositive;
        }

        public double getTrueNegative() {
            return trueNegative;
        }

        public Map<Integer, Double> getFalseNegative() {
            return falseNegative;
        }

        public void addTruePositive() {
            this.truePositive++;
        }

        public void addFalsePositive(int label) {
            if (falsePositive.containsKey(label)) {
                falsePositive.put(label, falsePositive.get(label) + 1);
            } else {
                falsePositive.put(label, 1.0);
            }
        }

        public void addTrueNegative() {
            this.trueNegative++;
        }

        public void addFalseNegative(int label) {
            if (falseNegative.containsKey(label)) {
                falseNegative.put(label, falseNegative.get(label) + 1);
            } else {
                falseNegative.put(label, 1.0);
            }
        }

        public double totalFalsePositive() {
            double ret = 0;
            ret = this.falsePositive.keySet().stream().map((key) -> this.falsePositive.get(key)).reduce(ret, (accumulator, _item) -> accumulator + _item);
            return ret;
        }

        public double totalFalseNegative() {
            double ret = 0;
            ret = this.falseNegative.keySet().stream().map((key) -> this.falseNegative.get(key)).reduce(ret, (accumulator, _item) -> accumulator + _item);
            return ret;
        }

        public double totalAcumulado() {
            return truePositive + trueNegative + totalFalseNegative() + totalFalsePositive();
        }

        public double accuracy() {
            return (this.getTruePositive() + this.getTrueNegative()) / this.totalAcumulado();
        }

        public double precision() {
            return this.truePositive / (this.truePositive + this.totalFalsePositive());
        }

        public double recall() {
            return this.truePositive / (this.truePositive + this.totalFalseNegative());
        }

        public double f1() {
            double prec = this.precision();
            double rec = this.recall();
            return 2 * (prec * rec / (prec + rec));
        }
    }
}

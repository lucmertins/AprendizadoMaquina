package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
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
    private final String label;

    public ExecuteAvaliacao(FileWriter outFile, String label) {
        this.outFile = outFile;
        this.label = label;
    }

    public void run(File fileTest, SamplesParameters samplesParameters, String filePerceptron) throws IOException, ClassNotFoundException {
        this.run(fileTest, samplesParameters, filePerceptron, null);
    }

    public void run(File fileTest, SamplesParameters samplesParameters, String filePerceptron, Perceptron.AlgorithmSimoid algorithm) throws IOException, ClassNotFoundException {
        Samples samples = new Samples(samplesParameters, new FunctionSampleOut() {
            @Override
            public void prepare(String value, Sample sample) {
                sample.addOut(value.equals(label) ? 0 : 1);
                sample.addOutOriginal(value);
            }
        });
        samples.avaliaFirstLine(fileTest);
        samples.open(fileTest);
        Sample sample;
        Perceptron perceptron = Perceptron.deserialize(filePerceptron);
        Acumulador acumulador = new Acumulador();

        while ((sample = samples.next()) != null) {
            perceptron.fill(sample);
            if (algorithm != null) {
                perceptron.setAlgorithm(algorithm);
            }
            double out = perceptron.out();
            if (label.equals(sample.getOutOriginal(1))) {
                if (out > 0.9) {
//                    System.out.printf("True Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                    acumulador.addTruePositive();
                } else {
//                    System.out.printf("False Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                    acumulador.addFalseNegative(sample.getOutOriginal(1));
                }
            } else if (out > 0.9) {
//                System.out.printf("False Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                acumulador.addFalsePositive(sample.getOutOriginal(1));
            } else {
//                System.out.printf("True Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                acumulador.addTrueNegative();
            }
        }
        samples.close();
        double accuracia = (acumulador.getTruePositive() + acumulador.getTrueNegative()) / acumulador.totalAcumulado();
        if (outFile != null) {
            outFile.write(String.format("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(),acumulador.totalFalseNegative()));
            for (String key : acumulador.getFalsePositive().keySet()) {
                outFile.write(String.format("FalsePositivo achou que %s era %.0f %f vezes \n", key, label, acumulador.getFalsePositive().get(key)));
            }
            for (String key : acumulador.getFalseNegative().keySet()) {
                outFile.write(String.format("FalseNegative achou que %s era %.0f %f vezes \n", key, label, acumulador.getFalseNegative().get(key)));
            }
            outFile.write(String.format("Acuracia [%.12f]\n", accuracia));
        } else {
            System.out.printf("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f] \n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(),acumulador.totalFalseNegative());
            for (String key : acumulador.getFalsePositive().keySet()) {
                System.out.printf("FalsePositivo achou que %s era %s %f vezes \n", key, label, acumulador.getFalsePositive().get(key));
            }
            for (String key : acumulador.getFalseNegative().keySet()) {
                System.out.printf("FalseNegative não achou que %s era %s %f vezes \n", key, label, acumulador.getFalseNegative().get(key));
            }
            System.out.printf("Acuracia [%f]\n", accuracia);
        }
    }

    private class Acumulador {

        private double truePositive;
        private double trueNegative;
        private final Map<String, Double> falsePositive = new HashMap<>();
        private final Map<String, Double> falseNegative = new HashMap<>();

        public Acumulador() {
        }

        public double getTruePositive() {
            return truePositive;
        }

        public Map<String, Double> getFalsePositive() {
            return falsePositive;
        }

        public double getTrueNegative() {
            return trueNegative;
        }

        public Map<String, Double> getFalseNegative() {
            return falseNegative;
        }

        public void addTruePositive() {
            this.truePositive++;
        }

        public void addFalsePositive(String label) {
            if (falsePositive.containsKey(label)) {
                falsePositive.put(label, falsePositive.get(label) + 1);
            } else {
                falsePositive.put(label, 1.0);
            }
        }

        public void addTrueNegative() {
            this.trueNegative++;
        }

        public void addFalseNegative(String label) {
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
    }
}

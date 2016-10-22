package br.com.mertins.ufpel.avaliacao.perceptron;

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
    private final double label;
    
    public ExecuteAvaliacao(FileWriter outFile, double label) {
        this.outFile = outFile;
        this.label = label;
    }
    
    public void run(File fileTest, SamplesParameters samplesParameters, String filePerceptron) throws IOException, ClassNotFoundException {
        this.run(fileTest, samplesParameters, filePerceptron, null);
    }
    
    public void run(File fileTest, SamplesParameters samplesParameters, String filePerceptron, Perceptron.AlgorithmSimoid algorithm) throws IOException, ClassNotFoundException {
        Samples samples = new Samples(samplesParameters);
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
            if (sample.getOut(1) == label) {
                if (out > 0.9) {
//                    System.out.printf("True Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                    acumulador.addTruePositive();
                } else {
//                    System.out.printf("False Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                    acumulador.addFalseNegative();
                }
            } else if (out > 0.9) {
//                System.out.printf("False Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                acumulador.addFalsePositive(String.format("%f", sample.getOut(1)));
            } else {
//                System.out.printf("True Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                acumulador.addTrueNegative();
            }
        }
        samples.close();
        double accuracia = (acumulador.getTruePositive() + acumulador.getTrueNegative()) / acumulador.totalAcumulado();
        if (outFile != null) {
            outFile.write(String.format("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(), acumulador.getFalseNegative()));
            for (String key : acumulador.getFalsePositive().keySet()) {
                outFile.write(String.format("FalsePositivo achou que %s era %.0f %f vezes \n", key, label, acumulador.getFalsePositive().get(key)));
            }
            outFile.write(String.format("Acuracia [%.12f]\n", accuracia));
        } else {
            System.out.printf("truePositive [%f] trueNegative [%f] falsePositice [%f] falseNegative [%f]\n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(), acumulador.getFalseNegative());
            for (String key : acumulador.getFalsePositive().keySet()) {
                System.out.printf("FalsePositivo achou que %s era %.0f %f vezes \n", key, label, acumulador.getFalsePositive().get(key));
            }
            System.out.printf("Acuracia [%f]\n", accuracia);
        }
    }
    
    private class Acumulador {
        
        private double truePositive;
        private double trueNegative;
        private final Map<String, Double> falsePositive = new HashMap<>();
        private double falseNegative;
        
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
        
        public double getFalseNegative() {
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
        
        public void addFalseNegative() {
            falseNegative++;
        }
        
        public double totalFalsePositive() {
            double ret = 0;
            ret = this.falsePositive.keySet().stream().map((key) -> this.falsePositive.get(key)).reduce(ret, (accumulator, _item) -> accumulator + _item);
            return ret;
        }
        
        public double totalAcumulado() {
            return truePositive + trueNegative + falseNegative + totalFalsePositive();
        }
    }
}

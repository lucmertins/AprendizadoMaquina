package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecuteAvaliacao {

    public void run(String fileTest, SamplesParameters samplesParameters, String filePerceptron) throws IOException, ClassNotFoundException {
        this.run(fileTest, samplesParameters, filePerceptron, null);
    }

    public void run(String fileTest, SamplesParameters samplesParameters, String filePerceptron, Perceptron.AlgorithmSimoid algorithm) throws IOException, ClassNotFoundException {
        File file = new File(fileTest);
        Samples samples = new Samples(samplesParameters);
        samples.avaliaFirstLine(file);
        samples.open(file);
        Sample sample;
        Perceptron perceptron = Perceptron.deserialize(filePerceptron);
        double truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
        while ((sample = samples.next()) != null) {
            perceptron.fill(sample);
            if (algorithm != null) {
                perceptron.setAlgorithm(algorithm);
            }
            double out = perceptron.out();
            if (sample.getValue() == 0) {
                if (out == 1) {
                    System.out.printf("True Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                    truePositive++;
                } else {
                    System.out.printf("False Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                    falseNegative++;
                }
            } else if (out == 1) {
                System.out.printf("False Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                falsePositive++;
            } else {
                System.out.printf("True Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                trueNegative++;
            }
        }
        samples.close();
        double accuracia = (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
        System.out.printf("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", truePositive, trueNegative, falsePositive, falseNegative);
        System.out.printf("Acuracia [%f]\n", accuracia);

    }
}

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

    public void testar() throws IOException, ClassNotFoundException {
        File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
        System.out.printf("Arquivo %s\n", file.getAbsolutePath());
        SamplesParameters samplesParameters = new SamplesParameters();
        samplesParameters.setNormalize(true);   // transforme atributos em 0 ou 1
        samplesParameters.setNegativeValue(0);
        samplesParameters.setPositiveValue(1);
        samplesParameters.setFirstLineAttribute(false);
        samplesParameters.setColumnLabel(0);
        Samples samples = new Samples(samplesParameters);
        samples.avaliaFirstLine(file);
        samples.open(file);
        Sample sample;
        Perceptron perceptron = Perceptron.deserialize("/home/mertins/IAPerceptron/20161013_095928/perceptron_1_5");
        double truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
        while ((sample = samples.next()) != null) {
            perceptron.fill(sample);
            perceptron.setAlgorithm(Perceptron.AlgorithmSimoid.HARD_0);
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

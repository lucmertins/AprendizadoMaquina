package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.Training;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Execute {

    public void treinamento() throws IOException {
        File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
        System.out.printf("Arquivo %s\n", file.getAbsolutePath());
        Samples samples = new Samples();
        samples.setNormalize(true);
        samples.setNegativeValue(0);
        samples.setPositiveValue(1);
        samples.setFirstLineAttribute(false);
        samples.avaliaFirstLine(file);
        samples.defineColumnLabel(0);
        List<Integer> remove = new ArrayList<>();
        samples.removeAttributesPos(remove);
        samples.open(file);
        samples.setTruePositive("0");
        Training training = new Training();
        Perceptron perceptronZero = training.withDelta(samples, 0.000005, 10, Perceptron.AlgorithmSimoid.HARD_0);
        Perceptron.serialize(perceptronZero, "/home/mertins/Documentos/tmp/perceptronZero.obj");
//        samples.reset();
//        samples.setTruePositive("1");
//        Perceptron perceptronOne = training.withPerceptron(samples, 0.005, 4, Perceptron.AlgorithmSimoid.LOGISTIC);
//        Perceptron.serialize(perceptronOne, "/home/mertins/Documentos/tmp/perceptronOne.obj");
//        samples.reset();
//        samples.setTruePositive("2");
//        Perceptron perceptronTwo = training.withPerceptron(samples, 0.005, 4, Perceptron.AlgorithmSimoid.LOGISTIC);
//        Perceptron.serialize(perceptronTwo, "/home/mertins/Documentos/tmp/perceptronTwo.obj");
        samples.close();
    }

    public void testar() throws IOException, ClassNotFoundException {
        File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
        System.out.printf("Arquivo %s\n", file.getAbsolutePath());
        Samples samples = new Samples();
        samples.setNegativeValue(0);
        samples.setPositiveValue(1);
        samples.setFirstLineAttribute(false);
        samples.avaliaFirstLine(file);
        samples.defineColumnLabel(0);
        samples.open(file);
        Sample sample;

        Perceptron perceptron = Perceptron.deserialize("/home/mertins/Documentos/tmp/perceptronZero.obj");
        int truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
        while ((sample = samples.next()) != null) {
            perceptron.fill(sample);
            perceptron.setAlgorithm(Perceptron.AlgorithmSimoid.LOGISTIC);
            double out = perceptron.out();
            System.out.printf("Sample [%f]   out [%f]\n", sample.getValue(), out);
            if (sample.getValue() == 0) {
                if (out == 1) {
                    truePositive++;
                } else {
                    trueNegative++;
                }
            } else if (out == 1) {
                falsePositive++;
            } else {
                falseNegative++;
            }
        }
        samples.close();
        System.out.printf("truePositive [%d] trueNegative [%d] falsePositive [%d] falseNegative [%d]\n", truePositive, trueNegative, falsePositive, falseNegative);

    }

    public static void main(String[] args) {
        try {
            new Execute().treinamento();
        } catch (IOException ex) {
            Logger.getLogger(Execute.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
        System.out.println();
        try {
            new Execute().testar();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Execute.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

}

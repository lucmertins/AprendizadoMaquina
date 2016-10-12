package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.Training;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Execute {

    public void treinamento(ObservatorTraining observator) throws IOException {
        Instant inicioTreinamento = Instant.now();
        File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
        System.out.printf("Arquivo %s\n", file.getAbsolutePath());
        Samples samples = new Samples();
//        samples.setNormalize(true);
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
        training.addListenerObservatorTraining(observator);
        Perceptron perceptronZero = training.withDelta(samples, 0.00000000000005, 1000, Perceptron.AlgorithmSimoid.LOGISTIC);
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
        Duration duration = Duration.between(inicioTreinamento, Instant.now());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
        System.out.printf("Tempo de treinamento [%s]\n", format);
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
        double truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
        while ((sample = samples.next()) != null) {
            perceptron.fill(sample);
            perceptron.setAlgorithm(Perceptron.AlgorithmSimoid.LOGISTIC);
            double out = perceptron.out();
//            System.out.printf("Sample [%f]   out [%f]\n", sample.getValue(), out);
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
        double accuracia = (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
        System.out.printf("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", truePositive, trueNegative, falsePositive, falseNegative);
        System.out.printf("Acuracia [%f]\n", accuracia);

    }

    public static void main(String[] args) {
        ObservatorTraining observator = new ObservatorTraining() {
            @Override
            public void register(Duration duration, int epoca, double errEpoca) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
                System.out.printf("Epoca [%d] errEpoca [%f]  %s\n", epoca, errEpoca, format);
            }
        };
        try {
            new Execute().treinamento(observator);
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

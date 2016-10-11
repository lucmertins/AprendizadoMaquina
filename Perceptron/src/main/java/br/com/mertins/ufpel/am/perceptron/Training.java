package br.com.mertins.ufpel.am.perceptron;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Training {

    public Perceptron withPerceptron(Samples samples, double learningRate, int epoca, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Perceptron perceptron = new Perceptron(algorithm);
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            perceptron.createIn(entradas);
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                Instant inicioEpoca = Instant.now();
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = 0.0;
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(0.0);
                }
                Sample sample;
                int exemplo = 1;
                double errEpoca = 0.0;
                while ((sample = samples.next()) != null) {
                    perceptron.fill(sample);
                    double err = sample.getValue() - perceptron.out();
                    errEpoca += err;
//                    System.out.printf("Epoca [%d]   Erro [%f] Exemplo [%d]\n", epocaTemp, err, exemplo++);
                    // recalcular o peso do bias 
                    pesoBias += learningRate * err * perceptron.getBias();
                    // recalcular pesos das entradas
                    for (int i = 0; i < entradas; i++) {
                        double pesoTemp = pesosTemp.get(i) + learningRate * err * perceptron.in(i + 1);
                        pesosTemp.set(i, pesoTemp);
                    }
                }
                errEpoca = errEpoca / (entradas + 1);
                // atualizar pesos no perceptron baseado no peso anterior
                perceptron.setBiasWeight(perceptron.getBiasWeight() + pesoBias);
//                System.out.printf("Novo Peso bias [%f]\n", perceptron.getBiasWeight());
                int pos = 1;
                for (Double value : pesosTemp) {
                    perceptron.updateWeight(pos, perceptron.in(pos) + value);
                    pos++;
                }
                samples.reset();
                System.out.printf("Epoca [%d] errEpoca [%f]  %s\n", epocaTemp, errEpoca, informe(inicioEpoca));
            }
        }
        return perceptron;
    }

    public Perceptron withDelta(List<Sample> samples, double learningRate, int epoca) {
        Perceptron perceptron = new Perceptron();
        int entradas = 0;
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.isEmpty()) {
            entradas = samples.get(0).amountIn();
            perceptron.createIn(entradas);
        }
        for (int epocaTemp = 0; epocaTemp < epoca; epocaTemp++) {
            Instant inicioEpoca = Instant.now();
            List<Double> pesosTemp = new ArrayList<>();
            double pesoBias = 0.0;
            for (int i = 1; i <= entradas; i++) {
                pesosTemp.add(0.0);
            }
            for (Sample sample : samples) {
               perceptron.fill(sample);
                double err = sample.getValue() - perceptron.sum();
//                System.out.printf("Epoca [%d]   Erro [%f]\n", epocaTemp, err);
                // recalcular o peso do bias 
                pesoBias += learningRate * err * perceptron.getBias();
                // recalcular pesos das entradas
                for (int i = 0; i < entradas; i++) {
                    double pesoTemp = pesosTemp.get(i) + learningRate * err * perceptron.in(i + 1);
                    pesosTemp.set(i, pesoTemp);
                }
            }
            // atualizar pesos no perceptron baseado no peso anterior
//            System.out.printf("Epoca [%d] Peso bias [%f] delta [%f] ", epocaTemp, perceptron.getBiasWeight(), pesoBias);
            perceptron.setBiasWeight(perceptron.getBiasWeight() + pesoBias);
//            System.out.printf("Novo Peso bias [%f]\n", perceptron.getBiasWeight());
            int pos = 1;
            for (Double value : pesosTemp) {
                perceptron.updateWeight(pos, perceptron.in(pos) + value);
                pos++;
            }
            System.out.printf("Epoca [%d] %s\n", epocaTemp, informe(inicioEpoca));
        }
        return perceptron;
    }

    public Perceptron withDelta(Samples samples, double learningRate, int epoca, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Perceptron perceptron = new Perceptron(algorithm);
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            perceptron.createIn(entradas);
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                Instant inicioEpoca = Instant.now();
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = 0.0;
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(0.0);
                }
                Sample sample;
                int exemplo = 1;
                double errEpoca = 0.0;
                while ((sample = samples.next()) != null) {
                    perceptron.fill(sample);
                    double err = sample.getValue() - perceptron.sum();
                    errEpoca += err;
//                    System.out.printf("Epoca [%d]   Erro [%f] Exemplo [%d]\n", epocaTemp, err, exemplo++);
                    // recalcular o peso do bias 
                    pesoBias += learningRate * err * perceptron.getBias();
                    // recalcular pesos das entradas
                    for (int i = 0; i < entradas; i++) {
                        double pesoTemp = pesosTemp.get(i) + learningRate * err * perceptron.in(i + 1);
                        pesosTemp.set(i, pesoTemp);
                    }
                }
                errEpoca = errEpoca / (entradas + 1);

                // atualizar pesos no perceptron baseado no peso anterior
                perceptron.setBiasWeight(perceptron.getBiasWeight() + pesoBias);
//                System.out.printf("Novo Peso bias [%f]\n", perceptron.getBiasWeight());
                int pos = 1;
                for (Double value : pesosTemp) {
                    perceptron.updateWeight(pos, perceptron.in(pos) + value);
                    pos++;
                }
                samples.reset();
                System.out.printf("Epoca [%d] errEpoca [%f]  %s\n", epocaTemp, errEpoca, informe(inicioEpoca));
            }
        }
        return perceptron;
    }

    public Perceptron withStochastic(Samples samples, double learningRate, int epoca, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Perceptron perceptron = new Perceptron(algorithm);
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            perceptron.createIn(entradas);
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                Instant inicioEpoca = Instant.now();
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = 0.0;
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(0.0);
                }
                Sample sample;
                int exemplo = 1;
                while ((sample = samples.next()) != null) {
                    perceptron.fill(sample);
                    double err = sample.getValue() - perceptron.sum();
//                    System.out.printf("Epoca [%d]   Erro [%f] Exemplo [%d]\n", epocaTemp, err, exemplo++);
                    // recalcular o peso do bias 
                    pesoBias += learningRate * err * perceptron.getBias();
                    // recalcular pesos das entradas
                    for (int i = 0; i < entradas; i++) {
                        double pesoTemp = pesosTemp.get(i) + learningRate * err * perceptron.in(i + 1);
                        pesosTemp.set(i, pesoTemp);
                    }
                    // atualizar pesos no perceptron baseado no peso anterior
//                System.out.printf("Epoca [%d] Peso bias [%f] delta [%f] ", epocaTemp, perceptron.getBiasWeight(), pesoBias);
                    perceptron.setBiasWeight(perceptron.getBiasWeight() + pesoBias);
//                System.out.printf("Novo Peso bias [%f]\n", perceptron.getBiasWeight());
                    int pos = 1;
                    for (Double value : pesosTemp) {
                        perceptron.updateWeight(pos, perceptron.in(pos) + value);
                        pos++;
                    }
                }
                samples.reset();
                System.out.printf("Epoca [%d] %s\n", epocaTemp, informe(inicioEpoca));
            }
        }
        return perceptron;
    }

    private String informe(Instant inicio) {
        Duration duration = Duration.between(inicio, Instant.now());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
        return format;
    }
}

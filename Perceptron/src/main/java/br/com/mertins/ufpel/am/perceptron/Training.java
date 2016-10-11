package br.com.mertins.ufpel.am.perceptron;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Training {

    public Perceptron withDelta(List<Sample> samples) {
        return this.withDelta(samples, 0.01, 1000);
    }

    public Perceptron withDelta(List<Sample> samples, double learningRate, int epoca) {
        Perceptron perceptron = new Perceptron();
        int entradas = 0;
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.isEmpty()) {
            entradas = samples.get(0).amountIn();
            for (int i = 1; i <= entradas; i++) {
                perceptron.addIn(0);
            }
        }
        for (int epocaTemp = 0; epocaTemp < epoca; epocaTemp++) {
            List<Double> pesosTemp = new ArrayList<>();
            double pesoBias = 0.0;
            for (int i = 1; i <= entradas; i++) {
                pesosTemp.add(0.0);
            }
            for (Sample sample : samples) {
                int pos = 1;
                for (Double value : sample.getIns()) {
                    perceptron.updateIn(pos++, value);
                }
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
        }
        return perceptron;
    }

    public Perceptron withDelta(Samples samples) throws IOException {
        return this.withDelta(samples, 0.01, 1000);
    }

    public Perceptron withDelta(Samples samples, double learningRate, int epoca) throws IOException {
        return this.withDelta(samples, learningRate, epoca, Perceptron.AlgorithmSimoid.HARD_1);
    }

    public Perceptron withDelta(Samples samples, double learningRate, int epoca, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Perceptron perceptron = new Perceptron(algorithm);
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            for (int i = 1; i <= entradas; i++) {
                perceptron.addIn(0);
            }
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = 0.0;
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(0.0);
                }
                Sample sample;
                int exemplo = 1;
                double errEpoca = 0.0;
                while ((sample = samples.next()) != null) {
                    int pos = 1;
                    for (Double value : sample.getIns()) {
                        perceptron.updateIn(pos++, value);
                    }
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
                System.out.printf("Epoca [%d] errEpoca [%f] \n", epocaTemp, errEpoca);
                // atualizar pesos no perceptron baseado no peso anterior
                perceptron.setBiasWeight(perceptron.getBiasWeight() + pesoBias);
//                System.out.printf("Novo Peso bias [%f]\n", perceptron.getBiasWeight());
                int pos = 1;
                for (Double value : pesosTemp) {
                    perceptron.updateWeight(pos, perceptron.in(pos) + value);
                    pos++;
                }
                samples.reset();
            }
        }
        return perceptron;
    }

    public Perceptron withStochastic(Samples samples, double learningRate, int epoca, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Perceptron perceptron = new Perceptron(algorithm);
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            for (int i = 1; i <= entradas; i++) {
                perceptron.addIn(0,0);
            }
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = 0.0;
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(0.0);
                }
                Sample sample;
                int exemplo = 1;
                while ((sample = samples.next()) != null) {
                    int pos = 1;
                    for (Double value : sample.getIns()) {
                        perceptron.updateIn(pos++, value);
                    }
                    double err = sample.getValue() - perceptron.sum();
                    System.out.printf("Epoca [%d]   Erro [%f] Exemplo [%d]\n", epocaTemp, err, exemplo++);
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
                    pos = 1;
                    for (Double value : pesosTemp) {
                        perceptron.updateWeight(pos, perceptron.in(pos) + value);
                        pos++;
                    }
                }
                samples.reset();
            }
        }
        return perceptron;
    }
}

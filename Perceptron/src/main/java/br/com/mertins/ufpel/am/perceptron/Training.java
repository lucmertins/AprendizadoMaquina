package br.com.mertins.ufpel.am.perceptron;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Training {
    
    private boolean blocbkIfBadErr = true;
    
    public Training(boolean blocbkIfBadErr) {
        this.blocbkIfBadErr = blocbkIfBadErr;
    }
    
    private ObservatorTraining observator = new ObservatorTraining() {
    };
    
    public Perceptron withPerceptron(Samples samples, double learningRate, int epoca, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Perceptron perceptron = new Perceptron(algorithm);
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            perceptron.createIn(entradas);
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                Instant inicioEpoca = Instant.now();
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = perceptron.getBiasWeight();
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(perceptron.weigth(i));
                }
                Sample sample;
                double errEpoca = 0.0;
                while ((sample = samples.next()) != null) {
                    perceptron.fill(sample);
                    double err = sample.getOut(1) - perceptron.out();
                    errEpoca += Math.pow(err, 2);
                    pesoBias += learningRate * err * perceptron.getBias();
                    // recalcular pesos das entradas
                    for (int i = 0; i < entradas; i++) {
                        double pesoTemp = pesosTemp.get(i) + learningRate * err * perceptron.in(i + 1);
                        pesosTemp.set(i, pesoTemp);
                    }
                }
                errEpoca = errEpoca / (entradas + 1);
                perceptron.setBiasWeight(pesoBias);
                int pos = 1;
                for (Double value : pesosTemp) {
                    perceptron.updateWeight(pos, value);
                    pos++;
                }
                samples.reset();
                register(inicioEpoca, epocaTemp, errEpoca);
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
            double pesoBias = perceptron.getBiasWeight();
            for (int i = 1; i <= entradas; i++) {
                pesosTemp.add(perceptron.weigth(i));
            }
            double errEpoca = 0.0;
            for (Sample sample : samples) {
                perceptron.fill(sample);
                double err = sample.getOut(1) - perceptron.sum();
                errEpoca += Math.pow(err, 2);
                pesoBias += learningRate * err * perceptron.getBias();
                for (int i = 0; i < entradas; i++) {
                    double pesoTemp = pesosTemp.get(i) + learningRate * err * perceptron.in(i + 1);
                    pesosTemp.set(i, pesoTemp);
                }
            }
            errEpoca = errEpoca / (entradas + 1);
            perceptron.setBiasWeight(pesoBias);
            int pos = 1;
            for (Double value : pesosTemp) {
                perceptron.updateWeight(pos, value);
                pos++;
            }
            register(inicioEpoca, epocaTemp, errEpoca);
        }
        return perceptron;
    }
    
    public Perceptron withDelta(Samples samples, double learningRate, double moment, int epoca, Perceptron.AlgorithmSimoid algorithm, FileWriter out) throws IOException {
        return this.withDelta(samples, learningRate, moment, epoca, new Perceptron(algorithm), out);
    }
    
    public Perceptron withDelta(Samples samples, double learningRate, double moment, int epoca, Perceptron perceptron, FileWriter out) throws IOException {
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            perceptron.createIn(entradas);
            int epocaTemp = 1;
            double lastErr = Double.POSITIVE_INFINITY;
            boolean segue = true;
            double deltaPesoBias = 0.0;
            List<Double> deltaPesos = new ArrayList<>();
            for (int i = 1; i <= entradas; i++) {
                deltaPesos.add(0.0);
            }
            while (segue && epocaTemp <= epoca) {
                double errEpoca = 0.0;
                Instant inicioEpoca = Instant.now();
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = perceptron.getBiasWeight();
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(perceptron.weigth(i));
                }
                Sample sample;
                while ((sample = samples.next()) != null) {
                    perceptron.fill(sample);
                    double err = sample.getOut(1) - perceptron.sum();
                    errEpoca += Math.pow(err, 2);
                    deltaPesoBias = learningRate * err * perceptron.getBias() + moment * deltaPesoBias;
                    pesoBias += deltaPesoBias;
                    for (int i = 0; i < entradas; i++) {
                        final double deltaTemp = learningRate * err * perceptron.in(i + 1) + moment * deltaPesos.get(i);
                        deltaPesos.set(i, deltaTemp);
                        pesosTemp.set(i, pesosTemp.get(i) + deltaTemp);
                    }
                }
                errEpoca = errEpoca / (entradas + 1);
                if (!this.blocbkIfBadErr || Math.abs(errEpoca) <= Math.abs(lastErr)) {
                    lastErr = errEpoca;
                    perceptron.setBiasWeight(pesoBias);
                    int pos = 1;
                    for (Double value : pesosTemp) {
                        perceptron.updateWeight(pos, value);
                        pos++;
                    }
                    samples.reset();
                    register(inicioEpoca, epocaTemp, errEpoca);
                    epocaTemp++;
                    
                } else {
                    out.write(String.format("Não melhorou: ErrEpoca [%.30f]      lastErr [%.30f]\n", errEpoca, lastErr));
                    segue = false;
                }
            }
        }
        return perceptron;
    }
    
    public Perceptron withStochastic(Samples samples, double learningRate, double moment, int epoca, Perceptron.AlgorithmSimoid algorithm, FileWriter out) throws IOException, ClassNotFoundException {
        return this.withStochastic(samples, learningRate, moment, epoca, new Perceptron(algorithm), out);
    }
    
    public Perceptron withStochastic(Samples samples, double learningRate, double moment, int epoca, Perceptron perceptron, FileWriter out) throws IOException, ClassNotFoundException {
        //preparar o percetpron com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.getAttributes().isEmpty()) {
            int entradas = samples.getAttributes().size();
            perceptron.createIn(entradas);
            Perceptron perceptronAnterior = perceptron.copy();
            int epocaTemp = 1;
            double lastErr = Double.POSITIVE_INFINITY;
            boolean segue = true;
            double deltaPesoBias = 0.0;
            List<Double> deltaPesos = new ArrayList<>();
            for (int i = 1; i <= entradas; i++) {
                deltaPesos.add(0.0);
            }
            while (segue && epocaTemp <= epoca) {
                double errEpoca = 0.0;
                Instant inicioEpoca = Instant.now();
                List<Double> pesosTemp = new ArrayList<>();
                double pesoBias = perceptron.getBiasWeight();
                for (int i = 1; i <= entradas; i++) {
                    pesosTemp.add(perceptron.weigth(i));
                }
                Sample sample;
                while ((sample = samples.next()) != null) {
                    perceptron.fill(sample);
                    double err = sample.getOut(1) - perceptron.sum();
                    errEpoca += Math.pow(err, 2);
                    deltaPesoBias = learningRate * err * perceptron.getBias() + moment * deltaPesoBias;
                    pesoBias += deltaPesoBias;
                    for (int i = 0; i < entradas; i++) {
                        final double deltaTemp = learningRate * err * perceptron.in(i + 1) + moment * deltaPesos.get(i);
                        deltaPesos.set(i, deltaTemp);
                        pesosTemp.set(i, pesosTemp.get(i) + deltaTemp);
                    }
                    perceptron.setBiasWeight(pesoBias);
                    int pos = 1;
                    for (Double value : pesosTemp) {
                        perceptron.updateWeight(pos, value);
                        pos++;
                    }
                }
                errEpoca = errEpoca / (entradas + 1);
                if (!this.blocbkIfBadErr || Math.abs(errEpoca) <= Math.abs(lastErr)) {
                    lastErr = errEpoca;
                    samples.reset();
                    register(inicioEpoca, epocaTemp, errEpoca);
                    epocaTemp++;
                    perceptronAnterior = perceptron.copy();
                } else {
                    out.write(String.format("Não melhorou: ErrEpoca [%.30f]      lastErr [%.30f]\n", errEpoca, lastErr));
                    segue = false;
                    perceptron = perceptronAnterior;
                }
                
            }
        }
        return perceptron;
    }
    
    public void addListenerObservatorTraining(ObservatorTraining observator) {
        this.observator = observator;
    }
    
    private void register(Instant inicio, int epoca, double errEpoca) {
        Duration duration = Duration.between(inicio, Instant.now());
        this.observator.register(duration, epoca, errEpoca);
    }
    
}

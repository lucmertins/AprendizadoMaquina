package br.com.mertins.ufpel.am.perceptron;

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
            double pesoBias=0.0;
            for (int i = 1; i <= entradas; i++) {
                pesosTemp.add(0.0);
            }
            for (Sample sample : samples) {
                int pos = 1;
                for (Double value : sample.getValues()) {
                    perceptron.updateIn(pos++, value);
                }
                double outFind = perceptron.sum();
                int outReal = sample.getValue();
                // recalcular o peso do bias 
                pesoBias+=learningRate * (outReal - outFind) * perceptron.getBias();
                // recalcular pesos das entradas
                pos = 1;
                for (int i = 0; i < entradas; i++) {
                    double pesoTemp = pesosTemp.get(i) + learningRate * (outReal - outFind) * perceptron.in(pos++);
                    pesosTemp.set(i, pesoTemp);
                }
            }
            // atualizar pesos no perceptron
            perceptron.setBiasWeight(pesoBias);
            int pos = 1;
            for (Double value : pesosTemp) {
                perceptron.updateWeight(pos++, value);
            }
        }
        return perceptron;
    }
}

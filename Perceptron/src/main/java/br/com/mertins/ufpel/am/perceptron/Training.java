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
        Perceptron neuronio = new Perceptron();  
        int entradas = 0;
        //preparar o neuronio com o numero de entradas adequados. Colocando pesos randomicos
        if (!samples.isEmpty()) {
            entradas = samples.get(0).amountIn();
            for (int i = 1; i <= entradas; i++) {
                neuronio.addIn(0);
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
                    neuronio.updateIn(pos++, value);
                }
                double outFind = neuronio.sum();
                int outReal = sample.getValue();
                //calcular o peso do bias tbm
                pesoBias+=learningRate * (outReal - outFind) * neuronio.getBias();
                
                pos = 1;
                for (int i = 0; i < entradas; i++) {
                    double pesoTemp = pesosTemp.get(i) + learningRate * (outReal - outFind) * neuronio.in(pos++);
                    pesosTemp.set(i, pesoTemp);
                }
            }
            neuronio.setBiasWeight(pesoBias);
            int pos = 1;
            for (Double value : pesosTemp) {
                neuronio.updateWeight(pos++, value);
            }
        }
        return neuronio;
    }
}

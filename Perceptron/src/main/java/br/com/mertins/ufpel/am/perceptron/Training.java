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
        Perceptron neuronio = new Perceptron(0.5);  // inicializa o perceptron com bias=0;
        int entradas = 0;
        //preparar o neuronio com o numero de entradas adequados. Colocando pesos iniciais zerados
        if (!samples.isEmpty()) {
            entradas = samples.get(0).amountIn();
            for (int i = 1; i <= entradas; i++) {
                neuronio.addIn(0, 0);
            }
        }

        for (int epocaTemp = 0; epocaTemp < epoca; epocaTemp++) {
            List<Double> pesosTemp = new ArrayList<>();
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
                pos = 1;
                for (int i = 0; i < entradas; i++) {
                    double pesoTemp = pesosTemp.get(i) + learningRate * (outReal - outFind) * neuronio.in(pos++);
                    pesosTemp.set(i, pesoTemp);
                }
            }
            int pos = 1;
            for (Double value : pesosTemp) {
                neuronio.updateWeight(pos++, value);
            }
        }
        return neuronio;
    }
}

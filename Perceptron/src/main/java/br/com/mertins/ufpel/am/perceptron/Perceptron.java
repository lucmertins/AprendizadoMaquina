package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mertins
 */
public class Perceptron {

    private static final Random RANDOM = new Random();
    private final List<Sinaps> sinapsList = new ArrayList<>();
    private int bias;
    private double biasWeight;

    public Perceptron() {
        this(1, Perceptron.random());
    }

    public Perceptron(int bias, double biasWeight) {
        this.bias = bias;
        this.biasWeight = biasWeight;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(int bias) {
        this.bias = bias;
    }

    public double getBiasWeight() {
        return biasWeight;
    }

    public void setBiasWeight(double biasWeight) {
        this.biasWeight = biasWeight;
    }

    /**
     * Retorna a posição da entrada
     *
     * @param in
     * @return
     */
    public int addIn(double in) {
        return this.addIn(in, Perceptron.random());
    }

    /**
     * Retorna a posição da entrada
     *
     * @param in
     * @param weight
     * @return
     */
    public int addIn(double in, double weight) {
        sinapsList.add(new Sinaps(in, weight));
        return sinapsList.size();
    }

    public int amountIn() {
        return this.sinapsList.size();
    }

    /**
     * Altera o valor da entrada, conforme a posição especificada
     *
     * @param pos
     * @param in
     * @return
     */
    public void updateIn(int pos, double in) {
        if (pos > 0 && pos <= sinapsList.size()) {
            Sinaps get = sinapsList.get(pos - 1);
            get.setIn(in);
        }
    }

    public void updateWeight(int pos, double in) {
        if (pos > 0 && pos <= sinapsList.size()) {
            Sinaps get = sinapsList.get(pos - 1);
            get.setWeight(in);
        }
    }

    public double in(int pos) {
        if (pos > 0 && pos <= sinapsList.size()) {
            Sinaps get = sinapsList.get(pos - 1);
            return get.getIn();
        }
        return 0;
    }

    public double weigth(int pos) {
        if (pos > 0 && pos <= sinapsList.size()) {
            Sinaps get = sinapsList.get(pos - 1);
            return get.getWeight();
        }
        return 0;
    }

    double sum() {
        double result = bias * biasWeight;
        result = sinapsList.stream().map((sin) -> sin.getIn() * sin.getWeight()).reduce(result, (accumulator, _item) -> accumulator + _item);
        return result;
    }

    /**
     * Saida após a função de ativação
     *
     * @return
     */
    public int out() {
        return sum() >= 0 ? 1 : -1;
    }

    private static double random() {
        double min = 0.001;
        double max = 0.999;
        double range = max - min;
        double scaled = RANDOM.nextDouble() * range;
        double shifted = scaled - min;
        return shifted;
    }

}

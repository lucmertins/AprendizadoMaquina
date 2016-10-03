package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Perceptron {

    private final List<Sinaps> sinapsList = new ArrayList<>();
    private double bias;

    public Perceptron(double bias) {
        this.bias = bias;
    }
    
    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    /**
     * Retorna a posição da entrada
     *
     * @param in
     * @return
     */
    public int addIn(double in) {
        return this.addIn(in, 1);
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

    double sum() {
        double result = 1 * bias;
        result = sinapsList.stream().map((sin) -> sin.getIn() * sin.getWeight()).reduce(result, (accumulator, _item) -> accumulator + _item);
        return result;
    }

    /**
     * Saida após a função de ativação
     *
     * @return
     */
    public int out() {
        return sum() > 0 ? 1 : 0;
    }
}

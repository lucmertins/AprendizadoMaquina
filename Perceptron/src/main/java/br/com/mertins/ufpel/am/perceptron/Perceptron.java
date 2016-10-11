package br.com.mertins.ufpel.am.perceptron;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mertins
 */
public class Perceptron implements Serializable {

    public enum AlgorithmSimoid {
        HARD_0, HARD_1, LOGISTIC, TANGEN
    }
    private static final Random RANDOM = new Random();
    private final List<Sinaps> sinapsList = new ArrayList<>();
    private int bias;
    private double biasWeight;
    private AlgorithmSimoid algorithm;

    public Perceptron() {
        this(1, Perceptron.random());
    }

    public Perceptron(AlgorithmSimoid algorithm) {
        this(1, Perceptron.random(), algorithm);
    }

    public Perceptron(int bias, double biasWeight) {
        this(bias, biasWeight, AlgorithmSimoid.HARD_1);
    }

    public Perceptron(int bias, double biasWeight, AlgorithmSimoid algorithm) {
        this.bias = bias;
        this.biasWeight = biasWeight;
        this.algorithm = algorithm;
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

    public AlgorithmSimoid getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmSimoid algorithm) {
        this.algorithm = algorithm;
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
        if (result == Double.NaN) {
            System.out.println("E agora?");
        }
        return result;
    }

    /**
     * Saida após a função de ativação
     *
     * @return
     */
    public double out() {
        switch (algorithm) {
            case HARD_0:
                return funcHard0();
            case HARD_1:
                return funcHard1();
            case LOGISTIC:
                return funcLogistic();
            case TANGEN:
                return funcTangentHiper();
            default:
                return funcHard1();
        }
    }

    private static double random() {
        double min = 0.0001;
        double max = 0.0999;
        double range = max - min;
        double scaled = RANDOM.nextDouble() * range;
        double shifted = scaled - min;
        return shifted;
    }

    public static void serialize(Perceptron perceptron, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(perceptron);
        }
    }

    public static Perceptron deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Perceptron) ois.readObject();
        }
    }

    private double funcHard0() {
        return 0.0 < sum() ? 1 : 0;
    }

    private double funcHard1() {
        return 0.0 < sum() ? 1 : -1;
    }

    private double funcLogistic() {
        return 1.0 / (1.0 + Math.exp(-sum()));
    }

    private double funcTangentHiper() {
        double negativeE = Math.exp(-sum());
        return (1.0 - negativeE) / (1.0 + negativeE);
    }

}

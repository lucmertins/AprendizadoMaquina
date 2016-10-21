package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class MLP {

    private final List<Double> ins = new ArrayList<>();
    private final List<Layer> layers = new ArrayList<>();
    private final List<Perceptron> outs = new ArrayList<>();

    /**
     * Retorna a posição da entrada
     *
     * @param in
     * @return
     */
    public int addIn(double in) {
        this.ins.add(in);
        return ins.size();
    }

    public int amountIn() {
        return this.ins.size();
    }

    public void updateIn(int pos, Double value) {
        if (pos > 0 && pos <= ins.size()) {
            ins.set(pos - 1, value);
        }
    }

    public void createIn(int size) {
        this.createIn(size, 0.0);
    }

    public void createIn(int size, double value) {
        this.ins.clear();
        for (int i = 1; i <= size; i++) {
            this.addIn(value);
        }
    }

    public void addHiddenLayer(int position, int amount, Perceptron.AlgorithmSimoid algorithm) {
        if (position > 0 && position <= this.layers.size()) {
            Layer layer = new Layer(position);
            for (int i = 0; i < amount; i++) {
                layer.add(new Perceptron(algorithm));
            }
        }
    }

    public void addOut(int amount, Perceptron.AlgorithmSimoid algorithm) {
        this.outs.clear();
        for (int i = 0; i < amount; i++) {
            this.outs.add(new Perceptron(algorithm));
        }
    }

    public void build() {
        int size = this.ins.size();
        for (Layer layer : layers) {
            for (Perceptron perceptron : layer.perceptrons) {
                perceptron.createIn(size);
                size = layer.getPerceptrons().size();
            }
        }
        for (Perceptron perceptron : outs) {
            perceptron.createIn(size);
        }
    }

    private class Layer {

        private final int position;
        private final List<Perceptron> perceptrons;

        public Layer(int position) {
            this(position, new ArrayList<>());
        }

        public Layer(int position, List<Perceptron> perceptrons) {
            this.position = position;
            this.perceptrons = perceptrons;
        }

        public int getPosition() {
            return position;
        }

        public List<Perceptron> getPerceptrons() {
            return perceptrons;
        }

        public void add(Perceptron perceptron) {
            this.perceptrons.add(perceptron);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + this.position;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Layer other = (Layer) obj;
            if (this.position != other.position) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.format("Layer %d", position);
        }

    }
}

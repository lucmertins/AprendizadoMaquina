package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 *
 * @author mertins
 */
public class MLP {

    private final List<Double> ins = new ArrayList<>();
    private final List<Layer> layers = new ArrayList<>();
    private final List<Perceptron> outs = new ArrayList<>();
    private boolean ready = false;

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
        ready = false;
        this.ins.clear();
        for (int i = 1; i <= size; i++) {
            this.addIn(value);
        }
    }

    public void addHiddenLayer(int position, int amount, Perceptron.AlgorithmSimoid algorithm) {
        this.addHiddenLayer(position, amount, 1, algorithm);
    }

    public void addHiddenLayer(int position, int amount, int bias, Perceptron.AlgorithmSimoid algorithm) {
        ready = false;
        if (position > 0 && position <= this.layers.size() + 1) {
            Layer layer = new Layer(position);
            for (int i = 0; i < amount; i++) {
                layer.add(new Perceptron(bias, algorithm));
            }
            this.layers.add(layer);
        }
    }

    public void addOut(int amount, Perceptron.AlgorithmSimoid algorithm) {
        this.addOut(amount, 1, algorithm);
    }

    public void addOut(int amount, int bias, Perceptron.AlgorithmSimoid algorithm) {
        ready = false;
        this.outs.clear();
        for (int i = 0; i < amount; i++) {
            this.outs.add(new Perceptron(bias, algorithm));
        }
    }

    public void connect() {
        int[] size = {this.ins.size()};
        layers.stream().map((layer) -> {
            layer.perceptrons.stream().forEach((perceptron) -> {
                perceptron.createIn(size[0]);
            });
            return layer;
        }).forEach((layer) -> {
            size[0] = layer.getPerceptrons().size();
        });
        outs.stream().forEach((Perceptron perceptron) -> {
            perceptron.createIn(size[0]);
        });
        ready = !this.ins.isEmpty() && !this.layers.isEmpty() && !this.outs.isEmpty();
    }

    public void process() {
        if (ready) {
            IntStream.range(0, this.ins.size()).forEach(i -> {
                this.layers.get(0).perceptrons.forEach(perceptron -> {
                    perceptron.updateIn(i + 1, this.ins.get(i));
                });
            });
        }
        final AtomicInteger posLayer = new AtomicInteger();
        int totalLayers = this.layers.size() - 1;
        this.layers.forEach(layer -> {
            if (posLayer.get() < totalLayers) {
                posLayer.incrementAndGet();
                final AtomicInteger posPerceptronOut = new AtomicInteger(1);
                layer.perceptrons.forEach(perceptronOut -> {
                    final double out = perceptronOut.out();
                    layers.get(posLayer.get()).perceptrons.forEach(perceptronIn -> {
                        perceptronIn.updateIn(posPerceptronOut.get(), out);
                    });
                    posPerceptronOut.incrementAndGet();
                });
            } else {
                final AtomicInteger posPerceptronOut = new AtomicInteger(1);
                layer.perceptrons.forEach(perceptronOut -> {
                    final double out = perceptronOut.out();
                    outs.forEach(perceptronIn -> {
                        perceptronIn.updateIn(posPerceptronOut.get(), out);
                    });
                    posPerceptronOut.incrementAndGet();
                });
            }
        });

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

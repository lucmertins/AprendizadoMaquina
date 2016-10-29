package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.OutPerceptron;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 *
 * @author mertins
 */
public class MLP implements Serializable {

    private final List<Double> ins = new ArrayList<>();
    private final List<LayerImplements> layers = new ArrayList<>();
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

    public void updateIn(Sample sample) {
        for (int i = 1; i <= sample.amountIn(); i++) {
            this.updateIn(i, sample.getIn(i));
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

    public int addHiddenLayer(int amount, Perceptron.AlgorithmSimoid algorithm) {
        return this.addHiddenLayer(amount, 1, algorithm);
    }

    public int addHiddenLayer(int amount, int bias, Perceptron.AlgorithmSimoid algorithm) {
        ready = false;
        LayerImplements layer = new LayerImplements(this.layers.size());
        for (int i = 0; i < amount; i++) {
            layer.add(new Perceptron(bias, algorithm));
        }
        this.layers.add(layer);
        return this.layers.size();
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

    public int amountOut() {
        return this.outs.size();
    }

    public int amountHiddenLayer() {
        return this.layers.size();
    }

    public int amountPerceptronsHiddenLayer(int pos) {
        if (pos > 0 && pos <= this.layers.size()) {
            return this.layers.get(pos - 1).amount();
        }
        return 0;
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

    public OutPerceptron[] process() {
        if (ready) {
            IntStream.range(0, this.ins.size()).forEach(i -> {
                this.layers.get(0).perceptrons.forEach(perceptron -> {
                    perceptron.updateIn(i + 1, this.ins.get(i));
                });
            });
        }
        int posLayer = 0;
        int totalLayers = this.layers.size() - 1;
        for (Layer layer : this.layers) {
            if (posLayer++ < totalLayers) {
                final AtomicInteger posPerceptronOut = new AtomicInteger(1);
                for (Perceptron perceptronOut : layer.getPerceptrons()) {
                    final double out = perceptronOut.out();
                    for (Perceptron perceptronIn : this.layers.get(posLayer).getPerceptrons()) {
                        perceptronIn.updateIn(posPerceptronOut.get(), out);
                    }
                    posPerceptronOut.incrementAndGet();
                }

            } else {
                int posPerceptronOut = 1;
                for (Perceptron perceptronOut : layer.getPerceptrons()) {
                    final double out = perceptronOut.out();
                    for (Perceptron perceptronIn : this.outs) {
                        perceptronIn.updateIn(posPerceptronOut, out);
                    }
                    posPerceptronOut++;
                }
            }
        }

        OutPerceptron[] results = new OutPerceptron[outs.size()];
        int[] pos = {0};
        outs.forEach(perceptron -> {
            results[pos[0]++] = perceptron.outs();
        });
        return results;
    }

    Layer getLayer(int pos) {
        if (pos > 0 && pos <= this.layers.size()) {
            return this.layers.get(pos - 1);
        }
        return null;
    }

    int maxElemLayersHiddenOut() {
        int result = outs.size();
        for (Layer layer : layers) {
            result = result > layer.amount() ? result : layer.amount();
        }
        return result;
    }

    Perceptron getOut(int pos) {
        if (pos > 0 && pos <= this.outs.size()) {
            return this.outs.get(pos - 1);
        }
        return null;
    }

    List<Perceptron> getOuts() {
        return this.outs;
    }

    public static void serialize(MLP network, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(network);
        }
    }

    public static MLP deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (MLP) ois.readObject();
        }
    }

    private class LayerImplements implements Layer, Serializable {

        private final int position;
        private final List<Perceptron> perceptrons;

        public LayerImplements(int position) {
            this(position, new ArrayList<>());
        }

        public LayerImplements(int position, List<Perceptron> perceptrons) {
            this.position = position;
            this.perceptrons = perceptrons;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public List<Perceptron> getPerceptrons() {
            return perceptrons;
        }

        public void add(Perceptron perceptron) {
            this.perceptrons.add(perceptron);
        }

        @Override
        public int amount() {
            return this.perceptrons.size();
        }

        @Override
        public Perceptron getPerceptron(int pos) {
            if (pos > 0 && pos <= this.perceptrons.size()) {
                return this.perceptrons.get(pos - 1);
            }
            return null;
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
            final LayerImplements other = (LayerImplements) obj;
            if (this.position != other.position) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return String.format("Layer %d", position);
        }

        @Override
        public double[] getOuts() {
            double[] ret = new double[this.perceptrons.size()];
            int i = 0;
            for (Perceptron perceptron : this.perceptrons) {
                ret[i++] = perceptron.out();
            }
            return ret;
        }

    }
}

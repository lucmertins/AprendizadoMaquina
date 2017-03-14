package br.com.mertins.ufpel.knn;

import br.com.mertins.ufpel.am.perceptron.Sample;

/**
 *
 * @author mertins
 */
public class Knn {

    private double[] attributes;
    private String label;

    public Knn(int size) {
        attributes = new double[size];
    }

    public Knn(int size, String label) {
        this(size);
        this.label = label;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[] attributes) {
        this.attributes = attributes;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double distance(Knn other) {
        // distancia euclidiana
        double[] otherAtt = other.getAttributes();
        double sum = 0;
        for (int i = 0; i < this.attributes.length; i++) {
            sum += Math.pow(this.attributes[i] - otherAtt[i], 2);
        }

        return Math.sqrt(sum);
    }

    public void load(Sample sample) {
        int pos = 0;
        for (Double value : sample.getIns()) {
            this.attributes[pos++] = value;
        }
    }

    public static Knn instance(Sample sample) {
        Knn knn = new Knn(sample.getIns().size());
        knn.load(sample);
        return knn;
    }

}

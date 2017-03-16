package br.com.mertins.ufpel.knn;

import br.com.mertins.ufpel.am.perceptron.Sample;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void setAttributes(List<Double> attributes) {
        Double[] temp=attributes.toArray(new Double[0]);
        int pos=0;
        this.attributes=new double[temp.length];
        for (Double value:temp){
            this.attributes[pos++]=value;
        }
    }

    public void load(Sample sample) {
        int pos = 0;
        for (Double value : sample.getIns()) {
            this.attributes[pos++] = value;
        }
    }

    public static Knn instanceLabel(Sample sample) {
        Knn knn = new Knn(sample.getIns().size());
        knn.load(sample);
        knn.label = sample.getOutOriginal(1);
        return knn;
    }

    public static int dominante(int k, List<KnnInformation> lista) {
        Map<String, Integer> contador = new HashMap();
        String labelDominante = null;
        for (KnnInformation inf : lista) {
            if (contador.containsKey(inf.getLabel())) {
                int get = contador.get(inf.getLabel()) + 1;
                contador.put(inf.getLabel(), get);
                if (k == get) {
                    labelDominante = inf.getLabel();
                    break;
                }
            } else {
                contador.put(inf.getLabel(), 1);
                if (k == 1) {
                    labelDominante = inf.getLabel();
                    break;
                }
            }
        } // avaliar o que fazer se não existir um label dominante
        return Integer.valueOf(labelDominante);
    }

}

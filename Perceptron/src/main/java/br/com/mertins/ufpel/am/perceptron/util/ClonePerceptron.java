package br.com.mertins.ufpel.am.perceptron.util;

import br.com.mertins.ufpel.am.perceptron.*;
import br.com.mertins.ufpel.am.perceptron.Perceptron.AlgorithmSimoid;
import static br.com.mertins.ufpel.am.perceptron.Perceptron.AlgorithmSimoid.HARD_0;
import static br.com.mertins.ufpel.am.perceptron.Perceptron.AlgorithmSimoid.HARD_1;
import static br.com.mertins.ufpel.am.perceptron.Perceptron.AlgorithmSimoid.LOGISTIC;
import static br.com.mertins.ufpel.am.perceptron.Perceptron.AlgorithmSimoid.TANGEN;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
public class ClonePerceptron implements Serializable {

    private static final long serialVersionUID = -1L;

    private static final Random RANDOM = new Random();
    private final List<CloneSinaps> sinapsList = new ArrayList<>();
    private int bias;
    private double biasWeight;
    private double deltaBiasWeight;
    private AlgorithmSimoid algorithm;
    private boolean update = true;
    private double valueSum = 0.0;

    public ClonePerceptron(Perceptron perceptron) {
        this.bias = (int) perceptron.getBias();
        this.biasWeight = perceptron.getBiasWeight();
        this.deltaBiasWeight = perceptron.getDeltaBiasWeight();
        this.algorithm = perceptron.getAlgorithm();
        List<Sinaps> paraArrumarBagunca = perceptron.paraArrumarBagunca();
        for (Sinaps sinaps : paraArrumarBagunca) {
            sinapsList.add(new CloneSinaps(sinaps));
        }

    }

    public ClonePerceptron copy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (ClonePerceptron) ois.readObject();
        }
    }

    public static void serialize(ClonePerceptron perceptron, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(perceptron);
        }
    }

    public static ClonePerceptron deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ClonePerceptron) ois.readObject();
        }
    }
    
    public  Perceptron desfazClone(){
        Perceptron perceptron=new Perceptron();
        perceptron.setBias(bias);
        perceptron.setBiasWeight(biasWeight);
        perceptron.setDeltaBiasWeight(deltaBiasWeight);
        perceptron.setAlgorithm(algorithm);
        perceptron.paraArrumarBagunca().clear();
        for (CloneSinaps cloneSinaps:sinapsList){
            perceptron.paraArrumarBagunca().add(cloneSinaps.desfazClone());
        }
        return perceptron;
    }

}

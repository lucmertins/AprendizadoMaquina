package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author mertins
 */
public class TrainingTest {

    public TrainingTest() {
    }

    @Test
    public void testBuildSamples() {
        List<Sample> samples = new ArrayList<>();
        for (int j = 0; j < 8; j++) {
            Sample sample = new Sample();
            for (int i = 0; i < 8; i++) {
                sample.addIn(i == j ? 1 : 0);
            }
            samples.add(sample);
        }
        int pos = 0;
        for (Sample sample : samples) {
            for (int i = 0; i < 8; i++) {
                sample.addOut(i == pos ? 1 : 0);
            }
            pos++;
        }
        samples.stream().forEach((sample) -> {
            for (int i = 0; i < 8; i++) {
                Assert.assertEquals(sample.getIn(i), sample.getOut(i));
            }
        });
    }

    @Test
    public void testWithBackPropagation() {
        List<Sample> samples = new ArrayList<>();
        for (int j = 0; j < 8; j++) {
            Sample sample = new Sample();
            for (int i = 0; i < 8; i++) {
                sample.addIn(i == j ? 1 : 0);
            }
            samples.add(sample);
        }
        int pos = 0;
        for (Sample sample : samples) {
            for (int i = 0; i < 8; i++) {
                sample.addOut(i == pos ? 1 : 0);
            }
            pos++;
        }

        MLP rede = new MLP();
        rede.createIn(8);
        rede.addHiddenLayer(1, 3, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.addOut(8, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.connect();
        Training treino = new Training(false);
        treino.withBackPropagation(rede, samples, 0.5, 1);
    }

}

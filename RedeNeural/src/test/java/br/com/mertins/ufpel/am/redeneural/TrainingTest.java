package br.com.mertins.ufpel.am.redeneural;

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
        samples.stream().forEach((sample) -> {
            for (int i = 0; i < 8; i++) {
                Assert.assertEquals(sample.getIn(i), sample.getOut(i));
            }
        });
    }

}

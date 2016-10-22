package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mertins
 */
public class MLPTest {

    public MLPTest() {
    }

    @Test
    public void testProcess() {
        MLP rede = new MLP();
        rede.createIn(8, 2.0);
        for (int i = 1; i < 9; i++) {
            rede.updateIn(i, Double.valueOf(i));
        }
        rede.addHiddenLayer(1, 4, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.addHiddenLayer(2, 3, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.addOut(8, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.connect();
        double[] process = rede.process();
    }

}

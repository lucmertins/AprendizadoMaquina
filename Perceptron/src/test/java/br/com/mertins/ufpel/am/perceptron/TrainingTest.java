package br.com.mertins.ufpel.am.perceptron;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author mertins
 */
public class TrainingTest {

    public TrainingTest() {
    }

    @Test
    public void testORWithDelta() {
        Training training = new Training();
        List<Sample> lista = new ArrayList<>();
        Sample sampleOR = new Sample();
        sampleOR.addIn(-1);
        sampleOR.addIn(-1);
        sampleOR.setValue(-1);
        lista.add(sampleOR);
        sampleOR = new Sample();
        sampleOR.addIn(1);
        sampleOR.addIn(-1);
        sampleOR.setValue(1);
        lista.add(sampleOR);
        sampleOR = new Sample();
        sampleOR.addIn(-1);
        sampleOR.addIn(1);
        sampleOR.setValue(1);
        lista.add(sampleOR);
        sampleOR = new Sample();
        sampleOR.addIn(1);
        sampleOR.addIn(1);
        sampleOR.setValue(1);
        lista.add(sampleOR);

        Perceptron neuronioOR = training.withDelta(lista, 0.1, 5);
        System.out.printf("OR Bias [%f] PesoBias [%f]\n", neuronioOR.getBias(), neuronioOR.getBiasWeight());
        for (int i = 1; i <= neuronioOR.amountIn(); i++) {
            System.out.printf("OR Peso [%d] = [%f] \n", i, neuronioOR.weigth(i));
        }
        neuronioOR.updateIn(1, -1);
        neuronioOR.updateIn(2, -1);
        assertEquals("Deve ser -1", -1, neuronioOR.out());
        neuronioOR.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioOR.out());
        neuronioOR.updateIn(1, -1);
        neuronioOR.updateIn(2, 1);
        assertEquals("Deve ser 1", 1, neuronioOR.out());
        neuronioOR.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioOR.out());
    }

    @Test
    public void testANDWithDelta() {
        Training training = new Training();
        List<Sample> lista = new ArrayList<>();
        Sample sampleAND = new Sample();
        sampleAND.addIn(-1);
        sampleAND.addIn(-1);
        sampleAND.setValue(-1);
        lista.add(sampleAND);
        sampleAND = new Sample();
        sampleAND.addIn(1);
        sampleAND.addIn(-1);
        sampleAND.setValue(-1);
        lista.add(sampleAND);
        sampleAND = new Sample();
        sampleAND.addIn(-1);
        sampleAND.addIn(1);
        sampleAND.setValue(-1);
        lista.add(sampleAND);
        sampleAND = new Sample();
        sampleAND.addIn(1);
        sampleAND.addIn(1);
        sampleAND.setValue(1);
        lista.add(sampleAND);

        Perceptron neuronioAnd = training.withDelta(lista, 0.1, 5);
        System.out.printf("AND Bias [%f] PesoBias [%f]\n", neuronioAnd.getBias(), neuronioAnd.getBiasWeight());
        for (int i = 1; i <= neuronioAnd.amountIn(); i++) {
            System.out.printf("AND Peso [%d] = [%f] \n", i, neuronioAnd.weigth(i));
        }
        neuronioAnd.updateIn(1, -1);
        neuronioAnd.updateIn(2, -1);
        assertEquals("Deve ser -1", -1, neuronioAnd.out());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser -1", -1, neuronioAnd.out());
        neuronioAnd.updateIn(1, -1);
        neuronioAnd.updateIn(2, 1);
        assertEquals("Deve ser -1", -1, neuronioAnd.out());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioAnd.out());
    }

    @Test
    public void testSerialize() {
        Training training = new Training();
        List<Sample> lista = new ArrayList<>();
        Sample sampleAND = new Sample();
        sampleAND.addIn(-1);
        sampleAND.addIn(-1);
        sampleAND.setValue(-1);
        lista.add(sampleAND);
        sampleAND = new Sample();
        sampleAND.addIn(1);
        sampleAND.addIn(-1);
        sampleAND.setValue(-1);
        lista.add(sampleAND);
        sampleAND = new Sample();
        sampleAND.addIn(-1);
        sampleAND.addIn(1);
        sampleAND.setValue(-1);
        lista.add(sampleAND);
        sampleAND = new Sample();
        sampleAND.addIn(1);
        sampleAND.addIn(1);
        sampleAND.setValue(1);
        lista.add(sampleAND);
        Perceptron neuronioAnd = training.withDelta(lista, 0.1, 5);
        try {
            Perceptron.serialize(neuronioAnd, "/home/mertins/perceptronAnd.obj");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDeserialize() {
        try {
            Perceptron neuronioAnd = Perceptron.deserialize("/home/mertins/perceptronAnd.obj");
            System.out.printf("AND Bias [%f] PesoBias [%f]\n", neuronioAnd.getBias(), neuronioAnd.getBiasWeight());
            for (int i = 1; i <= neuronioAnd.amountIn(); i++) {
                System.out.printf("AND Peso [%d] = [%f] \n", i, neuronioAnd.weigth(i));
            }
            neuronioAnd.updateIn(1, -1);
            neuronioAnd.updateIn(2, -1);
            assertEquals("Deve ser -1", -1, neuronioAnd.out());
            neuronioAnd.updateIn(1, 1);
            assertEquals("Deve ser -1", -1, neuronioAnd.out());
            neuronioAnd.updateIn(1, -1);
            neuronioAnd.updateIn(2, 1);
            assertEquals("Deve ser -1", -1, neuronioAnd.out());
            neuronioAnd.updateIn(1, 1);
            assertEquals("Deve ser 1", 1, neuronioAnd.out());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

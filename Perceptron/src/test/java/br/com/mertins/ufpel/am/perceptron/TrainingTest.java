package br.com.mertins.ufpel.am.perceptron;

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

//    @Test
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

        Perceptron neuronioOr = training.withDelta(lista, 0.001, 115);
        System.out.printf("OR Bias [%f] PesoBias [%f]\n", neuronioOr.getBias(), neuronioOr.getBiasWeight());
        for (int i = 1; i <= neuronioOr.amountIn(); i++) {
            System.out.printf("OR Peso [%d] = [%f] \n", i, neuronioOr.weigth(i));
        }
        neuronioOr.updateIn(1, -1);
        neuronioOr.updateIn(2, -1);
        assertEquals("Deve ser -1", -1, Double.valueOf(neuronioOr.out()).intValue());
        neuronioOr.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, Double.valueOf(neuronioOr.out()).intValue());
        neuronioOr.updateIn(1, -1);
        neuronioOr.updateIn(2, 1);
        assertEquals("Deve ser 1", 1, Double.valueOf(neuronioOr.out()).intValue());
        neuronioOr.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, Double.valueOf(neuronioOr.out()).intValue());
    }

//    @Test
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

        Perceptron neuronioAnd = training.withDelta(lista, 0.001, 2150);
        System.out.printf("AND Bias [%f] PesoBias [%f]\n", neuronioAnd.getBias(), neuronioAnd.getBiasWeight());
        for (int i = 1; i <= neuronioAnd.amountIn(); i++) {
            System.out.printf("AND Peso [%d] = [%f] \n", i, neuronioAnd.weigth(i));
        }
        neuronioAnd.updateIn(1, -1);
        neuronioAnd.updateIn(2, -1);
        assertEquals("Deve ser -1", -1, Double.valueOf(neuronioAnd.out()).intValue());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser -1", -1, Double.valueOf(neuronioAnd.out()).intValue());
        neuronioAnd.updateIn(1, -1);
        neuronioAnd.updateIn(2, 1);
        assertEquals("Deve ser -1", -1, Double.valueOf(neuronioAnd.out()).intValue());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, Double.valueOf(neuronioAnd.out()).intValue());
    }

//    @Test
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
            Perceptron.serialize(neuronioAnd, "/home/mertins/Documentos/tmp/perceptronAnd.obj");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @Test
    public void testDeserialize() {
        try {
            Perceptron neuronioAnd = Perceptron.deserialize("/home/mertins/Documentos/tmp/perceptronAnd.obj");
            System.out.printf("AND Bias [%f] PesoBias [%f]\n", neuronioAnd.getBias(), neuronioAnd.getBiasWeight());
            for (int i = 1; i <= neuronioAnd.amountIn(); i++) {
                System.out.printf("AND Peso [%d] = [%f] \n", i, neuronioAnd.weigth(i));
            }
            neuronioAnd.updateIn(1, -1);
            neuronioAnd.updateIn(2, -1);
            assertEquals("Deve ser -1", -1, Double.valueOf(neuronioAnd.out()).intValue());
            neuronioAnd.updateIn(1, 1);
            assertEquals("Deve ser -1", -1, Double.valueOf(neuronioAnd.out()).intValue());
            neuronioAnd.updateIn(1, -1);
            neuronioAnd.updateIn(2, 1);
            assertEquals("Deve ser -1", -1, Double.valueOf(neuronioAnd.out()).intValue());
            neuronioAnd.updateIn(1, 1);
            assertEquals("Deve ser 1", 1, Double.valueOf(neuronioAnd.out()).intValue());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

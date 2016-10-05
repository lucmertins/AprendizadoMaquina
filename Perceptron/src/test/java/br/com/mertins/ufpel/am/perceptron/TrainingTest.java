package br.com.mertins.ufpel.am.perceptron;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author mertins
 */
public class TrainingTest {

    public TrainingTest() {
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
        Perceptron neuronioAnd = training.withDelta(lista, 0.01, 1000);

        for (int i = 1; i <= neuronioAnd.amountIn(); i++) {
            System.out.printf("Peso [%d] = [%f] \n", i, neuronioAnd.weigth(i));
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

//    @Test
    public void testXORWithDelta() {

        Training training = new Training();

        List<Sample> lista = new ArrayList<>();
        Sample sampleXOR = new Sample();
        sampleXOR.addIn(-1);
        sampleXOR.addIn(-1);
        sampleXOR.setValue(1);
        lista.add(sampleXOR);
        sampleXOR = new Sample();
        sampleXOR.addIn(1);
        sampleXOR.addIn(-1);
        sampleXOR.setValue(-1);
        lista.add(sampleXOR);
        sampleXOR = new Sample();
        sampleXOR.addIn(-1);
        sampleXOR.addIn(1);
        sampleXOR.setValue(-1);
        lista.add(sampleXOR);
        sampleXOR = new Sample();
        sampleXOR.addIn(1);
        sampleXOR.addIn(1);
        sampleXOR.setValue(1);
        lista.add(sampleXOR);
        Perceptron neuronioXOR = training.withDelta(lista, 0.01, 1000);

        for (int i = 1; i <= neuronioXOR.amountIn(); i++) {
            System.out.printf("Peso [%d] = [%f] \n", i, neuronioXOR.weigth(i));
        }

        neuronioXOR.updateIn(1, -1);
        neuronioXOR.updateIn(2, -1);
        assertEquals("Deve ser 1", 1, neuronioXOR.out());
        neuronioXOR.updateIn(1, 1);
        assertEquals("Deve ser -1", -1, neuronioXOR.out());
        neuronioXOR.updateIn(1, -1);
        neuronioXOR.updateIn(2, 1);
        assertEquals("Deve ser -1", -1, neuronioXOR.out());
        neuronioXOR.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioXOR.out());
    }

}

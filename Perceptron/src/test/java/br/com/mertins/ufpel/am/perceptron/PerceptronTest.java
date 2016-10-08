package br.com.mertins.ufpel.am.perceptron;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mertins
 */
public class PerceptronTest {

    public PerceptronTest() {
    }

//    @Test
    public void testOr() {
        Perceptron neuronioOr = new Perceptron(1,0.5);
        assertEquals("inserindo primeira entrada com valor -1 ", 1, neuronioOr.addIn(0,0.5));
        assertEquals("inserindo segunda entrada com valor -1 ", 2, neuronioOr.addIn(0,0.5));
        assertEquals("Deve ser zero", -1, neuronioOr.out());
        neuronioOr.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioOr.out());
        neuronioOr.updateIn(1, 0);
        neuronioOr.updateIn(2, 1);
        assertEquals("Deve ser 1", 1, neuronioOr.out());
        neuronioOr.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioOr.out());
    }

//    @Test
    public void testAnd() {
        Perceptron neuronioAnd = new Perceptron(1,1);
        assertEquals("inserindo primeira entrada com valor -1 peso 1 pois não tem treinamento", 1, neuronioAnd.addIn(-1,0.5));
        assertEquals("inserindo segunda entrada com valor -1 peso 1 pois não tem treinamento", 2, neuronioAnd.addIn(-1,0.5));
        assertEquals("Deve ser -1", -1, neuronioAnd.out());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser -1", -1, neuronioAnd.out());
        neuronioAnd.updateIn(1, -1);
        neuronioAnd.updateIn(2, 1);
        assertEquals("Deve ser -1", -1, neuronioAnd.out());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioAnd.out());
    }

}
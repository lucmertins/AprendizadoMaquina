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

    @Test
    public void testOr() {
        Perceptron neuronioOr = new Perceptron(0);
        assertEquals("inserindo primeira entrada com valor 0 peso default", 1, neuronioOr.addIn(0));
        assertEquals("inserindo segunda entrada com valor 0 peso default", 2, neuronioOr.addIn(0));
        assertEquals("Deve ser zero", 0, neuronioOr.out());
        neuronioOr.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioOr.out());
        neuronioOr.updateIn(1, 0);
        neuronioOr.updateIn(2, 1);
        assertEquals("Deve ser 1", 1, neuronioOr.out());
        neuronioOr.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioOr.out());
    }

    @Test
    public void testAnd() {

        // sem mexer na função de ativação, precisamos colocar as referencias como valores negados 0 passa a ser -1
        // não foi necessário modificar os pesos
        Perceptron neuronioAnd = new Perceptron(0);
        assertEquals("inserindo primeira entrada com valor 0 peso default", 1, neuronioAnd.addIn(-1));
        assertEquals("inserindo segunda entrada com valor 0 peso default", 2, neuronioAnd.addIn(-1));
        assertEquals("Deve ser zero", 0, neuronioAnd.out());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser 0", 0, neuronioAnd.out());
        neuronioAnd.updateIn(1, -1);
        neuronioAnd.updateIn(2, 1);
        assertEquals("Deve ser 0", 0, neuronioAnd.out());
        neuronioAnd.updateIn(1, 1);
        assertEquals("Deve ser 1", 1, neuronioAnd.out());
    }

}

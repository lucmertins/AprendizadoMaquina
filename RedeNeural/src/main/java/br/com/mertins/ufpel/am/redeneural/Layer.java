package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import java.util.List;

/**
 *
 * @author mertins
 */
public interface Layer {

    int getPosition();

    int amount();

    Perceptron getPerceptron(int pos);
    
    List<Perceptron> getPerceptrons();
    
    double[] getOuts();
}

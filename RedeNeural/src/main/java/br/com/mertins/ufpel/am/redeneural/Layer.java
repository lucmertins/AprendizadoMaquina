package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;

/**
 *
 * @author mertins
 */
public interface Layer {

    int getPosition();

    int amount();

    Perceptron getPerceptron(int pos);
    
    double[] getOuts();
}

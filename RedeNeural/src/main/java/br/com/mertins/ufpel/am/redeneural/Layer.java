package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mertins
 */
public interface Layer extends Serializable{

    int getPosition();

    int amount();

    Perceptron getPerceptron(int pos);
    
    List<Perceptron> getPerceptrons();
    
    double[] getOuts();
}

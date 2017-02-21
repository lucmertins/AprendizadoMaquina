package br.com.mertins.ufpel.am.perceptron.util;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import java.io.IOException;

/**
 *
 * @author mertins
 */
public class ArrumaBaguncaSerialID {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
// Código para gerar cópia clonada
//        String path = "/Users/mertins/IAPerceptron/20170218_100013";
//        for (int j = 4; j < 10; j++) {
//            for (int i = 1; i < 7; i++) {
//                Perceptron perceptron = Perceptron.deserialize(String.format("%s/perceptron_%d_%d", path, j, i));
//                ClonePerceptron.serialize(ClonePerceptron(perceptron), String.format("%s/cloneperceptron_%d_%d", path, j, i));
//            }
//        }

// Código para transformar clonado novamente na classe certa
        String path = "/Users/mertins/IAPerceptron/Clone20170218_100013";
        for (int j = 0; j < 10; j++) {
            for (int i = 1; i < 7; i++) {
                ClonePerceptron clone = ClonePerceptron.deserialize(String.format("%s/cloneperceptron_%d_%d", path, j, i));
                Perceptron.serialize(clone.desfazClone(), String.format("%s/perceptron_%d_%d", path, j, i));
            }
        }

    }
}

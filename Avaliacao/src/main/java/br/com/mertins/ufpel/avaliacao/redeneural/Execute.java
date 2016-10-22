package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import java.io.File;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void treinamento(SamplesParameters parameters, boolean blocbkIfBadErr) {
//        try {
        File fileTreinamento = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
        File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");

        ExecTreinamento exeTreino = new ExecTreinamento();
        MLP rede = new MLP();
        rede.createIn(8, 2.0);

        for (int i = 1; i < 9; i++) {
            rede.updateIn(i, Double.valueOf(i));
        }

        rede.addHiddenLayer(1, 4, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.addHiddenLayer(2, 3, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.addOut(8, 0, Perceptron.AlgorithmSimoid.LOGISTIC);
        rede.connect();
        rede.process();
        System.out.println("Feito");
    }

    public static void main(String[] args) {
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(false);   // transforme atributos em 0 ou 1
        parameters.setNegativeValue(0);
        parameters.setPositiveValue(1);
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
        Execute.treinamento(parameters, false);
    }
}

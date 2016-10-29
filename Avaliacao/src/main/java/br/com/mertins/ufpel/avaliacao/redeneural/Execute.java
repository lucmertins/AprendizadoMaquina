package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void treinamento(SamplesParameters parameters, boolean blocbkIfBadErr) {
        try {
            File fileTreinamento = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            ExecTreinamento exeTreino = new ExecTreinamento();
            int numParametros = exeTreino.open(parameters, fileTreinamento, fileTest);
            MLP rede = new MLP();
            rede.createIn(numParametros);
            rede.addHiddenLayer(200, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.addHiddenLayer(30, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.addOut(10, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.connect();
            exeTreino.run(blocbkIfBadErr, 0.005, 0.6, 5, rede);
        } catch (IOException ex) {
            Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public static void main(String[] args) {
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(true);   // transforme atributos em 0 ou 1
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
        Execute.treinamento(parameters, false);
    }
}

package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.Treinamento;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void treinamento(SamplesParameters parameters, boolean blocbkIfBadErr, Treinamento treinamento) {
        try {
            File fileTreinamento = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
//            File fileTreinamento = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
//            File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            ExecTreinamento exeTreino = new ExecTreinamento();
//            exeTreino.open(parameters, fileTreinamento, fileTest, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
            exeTreino.open(parameters, fileTreinamento, fileTest, new String[]{"0"});
            exeTreino.run(blocbkIfBadErr, 0.000000000005, 0.6, 1000, 6, treinamento, Perceptron.AlgorithmSimoid.HARD_0);
        } catch (IOException ex) {
            Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public static void avaliacao(SamplesParameters parameters) {
        try {
            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
//            File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            ExecuteAvaliacao aval = new ExecuteAvaliacao(null, 5);
            aval.run(fileTest, parameters, "/home/mertins/IAPerceptron/20161020_185511/perceptron_5_7", Perceptron.AlgorithmSimoid.HARD_0);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ExecuteAvaliacao.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    public static void main(String[] args) {
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(false);   // transforme atributos em 0 ou 1
//        parameters.setNegativeValue(0);
//        parameters.setPositiveValue(1);
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
        Execute.treinamento(parameters, true, Treinamento.DELTA);
//        Execute.avaliacao(parameters);
    }

}

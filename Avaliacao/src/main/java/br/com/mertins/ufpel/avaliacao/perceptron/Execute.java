package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void treinamento(SamplesParameters parameters) {
        try {
            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            ExecTreinamento treinamento = new ExecTreinamento();
            treinamento.open(parameters, file, new String[]{"0", "1", "2", "3"});
            treinamento.run(true, 0.0000000005, 500, 10, Perceptron.AlgorithmSimoid.LOGISTIC);
        } catch (IOException ex) {
            Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public static void avaliacao(SamplesParameters parameters) {
        try {
            new ExecuteAvaliacao().run("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv", parameters, "/home/mertins/IAPerceptron/20161013_095928/perceptron_1_5");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ExecuteAvaliacao.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    public static void main(String[] args) {

        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(false);   // transforme atributos em 0 ou 1
        parameters.setNegativeValue(0);
        parameters.setPositiveValue(1);
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
//        if (args.length == 1) {
//            switch (args[0].toLowerCase()) {
//                case "treinamento":
//                    Execute.treinamento(parameters);
//                    break;
//                case "teste":
//                    Execute.avaliacao(parameters);
//                    break;
//            }
//        }
//        Execute.treinamento(parameters);
        Execute.avaliacao(parameters);
    }

}

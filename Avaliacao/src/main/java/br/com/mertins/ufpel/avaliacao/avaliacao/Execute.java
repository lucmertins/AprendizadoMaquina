package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Samples;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void treinamento() {
        try {
            Samples samples = new Samples();
            samples.setNormalize(true);   // transforme atributos em 0 ou 1
            samples.setNegativeValue(0);
            samples.setPositiveValue(1);
            samples.setFirstLineAttribute(false);
            samples.defineColumnLabel(0);
            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            ExecTreinamento treinamento = new ExecTreinamento();
            treinamento.open(samples, file, "0");
            treinamento.run(0.00000005, 200, 5, Perceptron.AlgorithmSimoid.LOGISTIC);
            treinamento.close();
        } catch (IOException ex) {
            Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public static void avaliacao() {
        System.out.println();
        try {
            new ExecuteAvaliacao().testar();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ExecuteAvaliacao.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    public static void main(String[] args) {
//        if (args.length == 1) {
//            switch (args[0].toLowerCase()) {
//                case "treinamento":
//                    Execute.treinamento();
//                    break;
//                case "teste":
//                    Execute.avaliacao();
//                    break;
//            }
//        }
        Execute.treinamento();
//        Execute.avaliacao();
    }

}

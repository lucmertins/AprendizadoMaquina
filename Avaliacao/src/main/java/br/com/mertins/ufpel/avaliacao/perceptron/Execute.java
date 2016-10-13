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

    public static void treinamento() {
        try {
            SamplesParameters samples = new SamplesParameters();
            samples.setNormalize(false);   // transforme atributos em 0 ou 1
            samples.setNegativeValue(0);
            samples.setPositiveValue(1);
            samples.setFirstLineAttribute(false);
            samples.setColumnLabel(0);
            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            ExecTreinamento treinamento = new ExecTreinamento();
            treinamento.open(samples, file, new String[]{"0", "1","2", "3"});
            treinamento.run(true,0.00000005, 500, 10, Perceptron.AlgorithmSimoid.LOGISTIC);
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

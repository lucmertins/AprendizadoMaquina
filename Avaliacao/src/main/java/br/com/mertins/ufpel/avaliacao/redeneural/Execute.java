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
//            File fileTreinamento = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
//            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            File fileTreinamento = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            ExecTreinamento exeTreino = new ExecTreinamento();
            int numParametros = exeTreino.open(parameters, fileTreinamento, fileTest);
            MLP rede = new MLP();
            rede.createIn(numParametros);
            rede.addHiddenLayer(48, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.addHiddenLayer(24, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.addHiddenLayer(12, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.addHiddenLayer(6, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.addOut(10, Perceptron.AlgorithmSimoid.LOGISTIC);
            rede.connect();
//            MLP rede = MLP.deserialize("/Users/mertins/IARedeNeural/20161029_212238/MLP_100");
            exeTreino.run(blocbkIfBadErr, 0.01, 0.6, 1000, rede);
//        } catch (ClassNotFoundException | IOException ex) {
        } catch (IOException ex) {
            Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public static void avaliacao(SamplesParameters parameters) {
        try {
            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
//            File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");

            ExecuteAvaliacao aval = new ExecuteAvaliacao(null);
            Accumulator[] acumuladores = aval.run(fileTest, parameters, "/home/mertins/IARedeNeural/20161030_233705/MLP_999");
//            aval.run(fileTest, parameters, "/Users/mertins/IARedeNeural/20161030_233705");

            ConfusionMatrix confusao = new ConfusionMatrix();
            confusao.resumo(acumuladores, System.out);
            confusao.matrix(acumuladores, System.out);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecuteAvaliacao.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    public static void main(String[] args) {
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(true);   // transforme atributos em 0 ou 1
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
//        Execute.treinamento(parameters, false);
        Execute.avaliacao(parameters);
    }
}
//
//
//   if (outFile != null) {
//                    outFile.write(String.format("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", acumulador.getTruePositive(), acumulador.getTrueNegative(), acumulador.totalFalsePositive(), acumulador.totalFalseNegative()));
//                    for (Integer key : acumulador.getFalsePositive().keySet()) {
//                        outFile.write(String.format("FalsePositivo achou que %d era %d [%.0f vezes] \n", key, i, acumulador.getFalsePositive().get(key)));
//                    }
//                    for (Integer key : acumulador.getFalseNegative().keySet()) {
//                        outFile.write(String.format("FalseNegative era %d acusou %d [%.0f vezes]  \n", key, i, acumulador.getFalseNegative().get(key)));
//                    }
//                    outFile.write(String.format("Acuracia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n", acumulador.accuracy(), acumulador.precision(), acumulador.recall(), acumulador.f1()));
//                } else {

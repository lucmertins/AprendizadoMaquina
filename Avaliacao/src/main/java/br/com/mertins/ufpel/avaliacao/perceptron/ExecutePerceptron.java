package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.Treinamento;
import br.com.mertins.ufpel.avaliacao.util.TrainerPerceptronProperty;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecutePerceptron {

    public void training(Properties properties) {
        try {
            TrainerPerceptronProperty propPerceptrons = new TrainerPerceptronProperty();
            propPerceptrons.setNormalize((String) properties.get("normalize"));
            propPerceptrons.setFirstLineAttribute((String) properties.get("firstlineattribute"));
            propPerceptrons.setColumnLabel((String) properties.get("columnlabel"));
            propPerceptrons.setFileTrainer((String) properties.get("filetrainer"));
            propPerceptrons.setFileTest((String) properties.get("filetest"));
            propPerceptrons.setRateTraining((String) properties.get("ratetraining"));
            propPerceptrons.setMoment((String) properties.get("moment"));
            propPerceptrons.setEpoch((String) properties.get("epoch"));
            propPerceptrons.setAlgorithm((String) properties.get("algorithm"));
            propPerceptrons.setTrainerType((String) properties.get("trainerType"));
            propPerceptrons.setLabels((String) properties.get("labels"));
            propPerceptrons.setAttempt((String) properties.get("attempt"));
            SamplesParameters parameters = new SamplesParameters();
            parameters.setNormalize(propPerceptrons.parseNormalize());
            parameters.setFirstLineAttribute(propPerceptrons.parseFirstLineAttribute());
            parameters.setColumnLabel(propPerceptrons.parseColumnLabel());
            File fileTreinamento = new File(propPerceptrons.getFileTrainer());
            File fileTest = new File(propPerceptrons.getFileTest());
            ExecTreinamento exeTreino = new ExecTreinamento();
            exeTreino.open(parameters, fileTreinamento, fileTest, propPerceptrons.parseLabels());
            exeTreino.run(propPerceptrons.parseBlockIfBadErr(), propPerceptrons.parseRateTraining(), propPerceptrons.parseMoment(),
                    propPerceptrons.parseEpoch(), propPerceptrons.parseAttempt(), propPerceptrons.parseTrainerType(), propPerceptrons.parseAlgorithm());
        } catch (Exception ex) {
            Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    @Deprecated
    public static void treinamento(SamplesParameters parameters, boolean blocbkIfBadErr, Treinamento treinamento) {
        try {
//            File fileTreinamento = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
//            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            File fileTreinamento = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_train.csv");
            File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            ExecTreinamento exeTreino = new ExecTreinamento();
            exeTreino.open(parameters, fileTreinamento, fileTest, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
//            exeTreino.open(parameters, fileTreinamento, fileTest, new String[]{"0", "3", "5", "8"});
            exeTreino.run(blocbkIfBadErr, 0.01, 0.7, 1000, 10, treinamento, Perceptron.AlgorithmSimoid.HARD_0);
        } catch (IOException ex) {
            Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public static void avaliacao(SamplesParameters parameters) {
        try {
            File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
//            File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            ExecuteAvaliacao aval = new ExecuteAvaliacao(null, "3");
            aval.run(fileTest, parameters, "/home/mertins/IAPerceptron/20161028_195844/perceptron_3_6", Perceptron.AlgorithmSimoid.HARD_0);
//            aval.run(fileTest, parameters, "/Users/mertins/IAPerceptron/20161027_204650/perceptron_3_5", Perceptron.AlgorithmSimoid.HARD_0);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ExecuteAvaliacao.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    public static void main(String[] args) {
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(true);   // transforme atributos em 0 ou 1
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
//        ExecutePerceptron.treinamento(parameters, true, Treinamento.ESTOCASTICO);
//        Execute.avaliacao(parameters);
    }

}

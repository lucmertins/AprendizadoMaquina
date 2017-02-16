package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.util.StringAsNumberComparator;
import br.com.mertins.ufpel.avaliacao.util.TrainerPerceptronProperty;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    public void evaluation(Properties properties) {
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
            propPerceptrons.setFolderPerceptrons((String) properties.get("folderPerceptrons"));

            for (String label : propPerceptrons.parseLabels()) {
                String nome = String.format("%s%sIA_avaliacao_%s.txt", propPerceptrons.getFolderPerceptrons(), File.separator, label);
                try (FileWriter outLog = new FileWriter(nome)) {
                    File folderMLPs = new File(propPerceptrons.getFolderPerceptrons());
                    String namefileBegin = String.format("perceptron_%s_", label);
                    List<File> perceptrons = Arrays.asList(folderMLPs.listFiles((File dir, String name) -> name.startsWith(namefileBegin)));
                    Collections.sort(perceptrons, new StringAsNumberComparator(namefileBegin));
                    for (File file : perceptrons) {
                        this.evalOne(label, file, propPerceptrons, outLog);
                        System.out.printf("Perceptrons a serem avaliados %s \n", file.getName());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ExecutePerceptron.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    private void evalOne(String label, File filePerceptron, TrainerPerceptronProperty propMPL, FileWriter outLog) throws IOException, ClassNotFoundException {
        outLog.write(String.format("\n\n%s\n", filePerceptron.getAbsoluteFile()));
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(propMPL.parseNormalize());
        parameters.setFirstLineAttribute(propMPL.parseFirstLineAttribute());
        parameters.setColumnLabel(propMPL.parseColumnLabel());

        ExecuteAvaliacao aval = new ExecuteAvaliacao(outLog, label);
        aval.run(new File(propMPL.getFileTest()), parameters, filePerceptron.getAbsolutePath(), propMPL.parseAlgorithm());

//        
//        ConfusionMatrix confusao = new ConfusionMatrix();
//        confusao.resumo(acumuladores1, outLog);
//        outLog.write(String.format("\n"));
//        confusao.matrix(acumuladores1, outLog);
//        outLog.write(String.format("\nGerais    Acurácia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n",
//                confusao.accuracy(acumuladores1), confusao.precision(acumuladores1), confusao.recall(acumuladores1), confusao.f1(acumuladores1)));
        outLog.flush();
    }
}

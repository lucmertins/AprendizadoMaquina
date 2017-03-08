package br.com.mertins.ufpel.avaliacao.util;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento;

/**
 *
 * @author mertins
 */
public class TrainerPerceptronProperty extends TrainerProperty {

    private String algorithm;
    private String trainerType;
    private String labels;
    private String attempt;
    private String folderPerceptrons;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getTrainerType() {
        return trainerType;
    }

    public void setTrainerType(String trainerType) {
        this.trainerType = trainerType;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }

    public String getFolderPerceptrons() {
        return folderPerceptrons;
    }

    public void setFolderPerceptrons(String folderPerceptrons) {
        this.folderPerceptrons = folderPerceptrons;
    }

    public Perceptron.AlgorithmSimoid parseAlgorithm() {
        return Perceptron.AlgorithmSimoid.valueOf(this.algorithm.trim());
    }

    public ExecTreinamento.Treinamento parseTrainerType() {
        return ExecTreinamento.Treinamento.valueOf(this.trainerType.trim());
    }

    public String[] parseLabels() {
        return this.labels.split("\\W+");
    }

    public int parseAttempt() {
        return Integer.parseInt(this.attempt.trim());
    }

}

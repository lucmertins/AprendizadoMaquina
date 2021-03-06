package br.com.mertins.ufpel.avaliacao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mertins
 */
public class TrainerMLPProperty extends TrainerProperty {

    private String hiddenLayer;
    private String outputLayer;
    private String folderMLPs;
    private String fileMLP;
    private String saveFrequence="1";
    private String fileAval;
    private String fileResult;
    private String fileOrigAval;

    public String getHiddenLayer() {
        return hiddenLayer;
    }

    public void setHiddenLayer(String hiddenLayer) {
        this.hiddenLayer = hiddenLayer;
    }

    public String getOutputLayer() {
        return outputLayer;
    }

    public void setOutputLayer(String outputLayer) {
        this.outputLayer = outputLayer;
    }

    public String getFolderMLPs() {
        return folderMLPs;
    }

    public void setFolderMLPs(String folderMLPs) {
        this.folderMLPs = folderMLPs;
    }

    public String getSaveFrequence() {
        return saveFrequence;
    }

    public void setSaveFrequence(String saveFrequence) {
        this.saveFrequence = saveFrequence;
    }

    public String getFileMLP() {
        return fileMLP;
    }

    public void setFileMLP(String fileMLP) {
        this.fileMLP = fileMLP;
    }

    public String getFileAval() {
        return fileAval;
    }

    public void setFileAval(String fileAval) {
        this.fileAval = fileAval;
    }

    public String getFileResult() {
        return fileResult;
    }

    public void setFileResult(String fileResult) {
        this.fileResult = fileResult;
    }

    public String getFileOrigAval() {
        return fileOrigAval;
    }

    public void setFileOrigAval(String fileOrigAval) {
        this.fileOrigAval = fileOrigAval;
    }

    public List<Layer> parseHiddenLayer() {
        List<Layer> lista = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\s*(\\s*\\w+\\s*,\\s*\\w+\\s*)\\s*");
        Matcher m = pattern.matcher(hiddenLayer);
        while (m.find()) {
            String[] varia = m.group().replace(" ", "").trim().split(",");
            lista.add(new Layer(Integer.parseInt(varia[0]), varia[1]));
        }
        return lista;
    }

    public Layer parseOutputLayer() {
        String[] varia = outputLayer.replace(" ", "").trim().split(",");
        return new Layer(Integer.parseInt(varia[0]), varia[1]);
    }

    public int parseSaveFrequence() {
        return Integer.parseInt(this.saveFrequence.trim());
    }
}

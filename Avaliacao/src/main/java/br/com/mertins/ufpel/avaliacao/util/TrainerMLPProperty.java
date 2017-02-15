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
}

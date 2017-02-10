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

    private String blockIfBadErr;
    private String hiddenLayer;
    private String outputLayer;
    private String normalize;
    private String rateTraining;
    private String moment;
    private String epoch;

    public String getBlockIfBadErr() {
        return blockIfBadErr;
    }

    public void setBlockIfBadErr(String blockIfBadErr) {
        this.blockIfBadErr = blockIfBadErr;
    }

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

    public String getNormalize() {
        return normalize;
    }

    public void setNormalize(String normalize) {
        this.normalize = normalize;
    }

    public String getRateTraining() {
        return rateTraining;
    }

    public void setRateTraining(String rateTraining) {
        this.rateTraining = rateTraining;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public boolean parseBlockIfBadErr() {
        return Boolean.parseBoolean(this.blockIfBadErr);
    }

    public boolean parseNormalize() {
        return Boolean.parseBoolean(this.normalize);
    }

    public double parseRateTraining() {
        return Double.parseDouble(this.rateTraining);
    }

    public double parseMoment() {
        return Double.parseDouble(this.moment);
    }

    public int parseEpoch() {
        return Integer.parseInt(this.epoch);
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

package br.com.mertins.ufpel.avaliacao.util;

/**
 *
 * @author mertins
 */
public abstract class TrainerProperty {

    protected String fileTrainer;
    protected String fileTest;
    protected String columnLabel;
    protected String firstLineAttribute;
    private String normalize;
    private String rateTraining;
    private String moment;
    private String epoch;
    private String blockIfBadErr;
    private String removeColumns = "0";
    private String fileResultColunaExtra = "false";

    public String getFileTrainer() {
        return fileTrainer;
    }

    public void setFileTrainer(String fileTrainer) {
        this.fileTrainer = fileTrainer;
    }

    public String getFileTest() {
        return fileTest;
    }

    public void setFileTest(String fileTest) {
        this.fileTest = fileTest;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getFirstLineAttribute() {
        return firstLineAttribute;
    }

    public void setFirstLineAttribute(String firstLineAttribute) {
        this.firstLineAttribute = firstLineAttribute;
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

    public String getBlockIfBadErr() {
        return blockIfBadErr;
    }

    public void setBlockIfBadErr(String blockIfBadErr) {
        this.blockIfBadErr = blockIfBadErr;
    }

    public String getRemoveColumns() {
        return removeColumns;
    }

    public void setRemoveColumns(String removeColumns) {
        this.removeColumns = removeColumns;
    }

    public String getFileResultColunaExtra() {
        return fileResultColunaExtra;
    }

    public void setFileResultColunaExtra(String fileResultColunaExtra) {
        this.fileResultColunaExtra = fileResultColunaExtra;
    }

    public int parseColumnLabel() {
        return Integer.parseInt(this.columnLabel.trim());
    }

    public boolean parseFirstLineAttribute() {
        return Boolean.parseBoolean(this.firstLineAttribute.trim());
    }

    public boolean parseNormalize() {
        return Boolean.parseBoolean(this.normalize.trim());
    }

    public double parseRateTraining() {
        return Double.parseDouble(this.rateTraining.trim());
    }

    public double parseMoment() {
        return Double.parseDouble(this.moment.trim());
    }

    public int parseEpoch() {
        return Integer.parseInt(this.epoch.trim());
    }

    public boolean parseBlockIfBadErr() {
        return Boolean.parseBoolean(this.blockIfBadErr.trim());
    }

    public int[] parseRemoveColumn() {
        String[] split = this.removeColumns.split("\\W+");
        int[] retorno = new int[split.length];
        int pos = 0;
        for (String elem : split) {
            retorno[pos++] = Integer.parseInt(elem.trim());
        }
        return retorno;
    }

    public boolean parseFileResultColunaExtra() {
        return Boolean.parseBoolean(this.fileResultColunaExtra.trim());
    }
}

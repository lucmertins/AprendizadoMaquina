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

    public int parseColumnLabel() {
        return Integer.parseInt(this.columnLabel);
    }

    public boolean parseFirstLineAttribute() {
        return Boolean.parseBoolean(this.firstLineAttribute);
    }
}

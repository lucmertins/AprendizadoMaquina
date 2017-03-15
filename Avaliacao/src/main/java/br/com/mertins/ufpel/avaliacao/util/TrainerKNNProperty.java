package br.com.mertins.ufpel.avaliacao.util;

/**
 *
 * @author mertins
 */
public class TrainerKNNProperty extends TrainerProperty {

    private String valueK;
    private String fileAval;
    private String fileResult;

    public String getValueK() {
        return valueK;
    }

    public void setValueK(String valueK) {
        this.valueK = valueK;
    }

    public String getFileResult() {
        return fileResult;
    }

    public void setFileResult(String fileResult) {
        this.fileResult = fileResult;
    }

    public String getFileAval() {
        return fileAval;
    }

    public void setFileAval(String fileAval) {
        this.fileAval = fileAval;
    }

    public int parseValueK() {
        return Integer.parseInt(this.valueK.trim());
    }

}

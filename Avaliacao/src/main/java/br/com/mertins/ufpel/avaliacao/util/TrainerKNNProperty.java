package br.com.mertins.ufpel.avaliacao.util;

/**
 *
 * @author mertins
 */
public class TrainerKNNProperty extends TrainerProperty {

    private String valueK;
    private String fileAval;
    private String fileResult;
    private String fileOrigAval;

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

    public String getFileOrigAval() {
        return fileOrigAval;
    }

    public void setFileOrigAval(String fileOrigAval) {
        this.fileOrigAval = fileOrigAval;
    }


    public int parseValueK() {
        return Integer.parseInt(this.valueK.trim());
    }

}

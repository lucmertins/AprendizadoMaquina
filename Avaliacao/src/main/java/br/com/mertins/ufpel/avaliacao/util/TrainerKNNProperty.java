package br.com.mertins.ufpel.avaliacao.util;

/**
 *
 * @author mertins
 */
public class TrainerKNNProperty extends TrainerProperty {
    private String valueK;

    public String getValueK() {
        return valueK;
    }

    public void setValueK(String valueK) {
        this.valueK = valueK;
    }

     public int parseValueK() {
        return Integer.parseInt(this.valueK.trim());
    }
}

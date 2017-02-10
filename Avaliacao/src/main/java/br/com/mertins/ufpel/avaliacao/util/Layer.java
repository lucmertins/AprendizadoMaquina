package br.com.mertins.ufpel.avaliacao.util;

/**
 *
 * @author mertins
 */
public class Layer {

    private int size;
    private String algoritm;

    public Layer(int size, String algoritm) {
        this.size = size;
        this.algoritm = algoritm;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAlgoritm() {
        return algoritm;
    }

    public void setAlgoritm(String algoritm) {
        this.algoritm = algoritm;
    }

}

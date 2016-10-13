package br.com.mertins.ufpel.am.perceptron;

/**
 *
 * @author mertins
 */
public class SamplesParameters {

    public SamplesParameters() {
    }
    
    private boolean normalize = false;
    private double negativeValue = -1.0;
    private double positiveValue = 1.0;
    private boolean firstLineAttribute = true;
    private int columnLabel;
    
    
//    
//     samples.setNormalize(true);   // transforme atributos em 0 ou 1
//            samples.setNegativeValue(0);
//            samples.setPositiveValue(1);
//            samples.setFirstLineAttribute(false);
//            samples.defineColumnLabel(0);

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public double getNegativeValue() {
        return negativeValue;
    }

    public void setNegativeValue(double negativeValue) {
        this.negativeValue = negativeValue;
    }

    public double getPositiveValue() {
        return positiveValue;
    }

    public void setPositiveValue(double positiveValue) {
        this.positiveValue = positiveValue;
    }

    public boolean isFirstLineAttribute() {
        return firstLineAttribute;
    }

    public void setFirstLineAttribute(boolean firstLineAttribute) {
        this.firstLineAttribute = firstLineAttribute;
    }

    public int getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(int columnLabel) {
        this.columnLabel = columnLabel;
    }
}

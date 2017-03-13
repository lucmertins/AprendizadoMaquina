package br.com.mertins.ufpel.am.perceptron;

/**
 *
 * @author mertins
 */
public class SamplesParameters {

    public SamplesParameters() {
    }
    
    private boolean normalize = false;
    private boolean firstLineAttribute = true;
    private int columnLabel;
    private int[] removeColumns;

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
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

    public int[] getRemoveColumns() {
        return removeColumns;
    }

    public void setRemoveColumns(int[] removeColumns) {
        this.removeColumns = removeColumns;
    }
    
}

package br.com.mertins.ufpel.am.preparacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Sample implements Serializable {

    private String delimiter = ",";
    private List<Attribute> attributes;
    private List<Register> registers;

    public List<Register> getRegisters() {
        return registers;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void addLineAttribute(String line) {
        attributes = new ArrayList<>();
        String[] split = line.split(this.delimiter);
        int pos = 0;
        for (String valor : split) {
            attributes.add(new Attribute(pos++, valor));
        }
    }

    public void addLineAttributeInstance(long pos, String line) {
        String[] split = line.split(this.delimiter);
        int posLine = 0;
        for (String valor : split) {
//            attributes.add(new Attribute(posLine++, valor));
        }
    }

}

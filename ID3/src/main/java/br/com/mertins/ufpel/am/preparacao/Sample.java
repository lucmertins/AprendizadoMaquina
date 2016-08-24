package br.com.mertins.ufpel.am.preparacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mertins
 */
public class Sample implements Serializable {

    private String delimiter = ",";
    private final List<Attribute> attributes = new ArrayList<>();
    private final List<Register> registers = new ArrayList<>();
    private final List<Integer> discardedColumns = new ArrayList<>();
    private final Set<Label> labels = new HashSet<>();
    private final int ColumnLabel;
    private String labelName;

    public Sample(int ColumnLabel) {
        this.ColumnLabel = ColumnLabel;
    }

    public List<Register> getRegisters() {
        return registers;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void addDiscardedColumns(int value) {
        this.discardedColumns.add(value);
    }

    private void addLineAttribute(String line) {
        String[] split = line.split(this.delimiter);
        int pos = 0;
        for (String valor : split) {
            if (!this.discardedColumns.contains(pos)) {
                if (this.ColumnLabel == pos) {
                    labelName = valor;
                } else {
                    attributes.add(new Attribute(pos++, valor));
                }
            } else {
                pos++;
            }
        }
    }

    private void addLineAttributeInstance(long pos, String line) {
        String[] split = line.split(this.delimiter);
        int posCol = 0;
        int posColReal = 0;
        Register register = new Register(pos);
        for (String valor : split) {
            if (!this.discardedColumns.contains(posColReal)) {
                if (this.ColumnLabel == posColReal) {
                    Label labelTemp = new Label(valor);
                    if (labels.contains(labelTemp)) {
                        labels.stream().filter((label) -> (label.equals(labelTemp))).forEach((label) -> {
                            register.setLabel(label);
                        });
                    } else {
                        labels.add(labelTemp);
                        register.setLabel(labelTemp);
                    }
                } else {
                    Attribute attribute = attributes.get(posCol++);
                    AttributeInstance addAttributeInstance = attribute.addAttributeInstance(valor);

                    register.addAttributesInstance(addAttributeInstance);
                }
            }
            posColReal++;
        }
        this.registers.add(register);
    }

    public void process(BufferedReader arquivo) throws IOException {
        String linha = arquivo.readLine();
        boolean firstLine = true;
        long pos = 0;
        while (linha != null) {
            if (firstLine) {
                this.addLineAttribute(linha);
                firstLine = false;
            } else {
                this.addLineAttributeInstance(pos, linha);
            }
            linha = arquivo.readLine();
            pos++;
        }
    }

}

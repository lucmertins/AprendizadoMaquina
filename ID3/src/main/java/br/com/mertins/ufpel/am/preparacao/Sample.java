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
    private List<Attribute> attributesOrigin = new ArrayList<>();
    private final List<Attribute> attributes = new ArrayList<>();
    private final List<Register> registers = new ArrayList<>();
    private final List<Integer> discardedColumns = new ArrayList<>();
    private final Set<Label> labels = new HashSet<>();
    private int columnLabel;
    private Attribute labelColumn;
    private String labelInstancePositive;

    private boolean firstLineAttribute = true;

    public Sample() {
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

    public List<Attribute> getAttributesOrigin() {
        return attributesOrigin;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void defineColumnLabel(int columnLabel,String labelInstancePositive) {
        this.columnLabel = columnLabel;
        this.labelInstancePositive = labelInstancePositive;
        this.getAttributesOrigin().forEach(atributo -> {
            if (atributo.getPosition() == columnLabel) {
                labelColumn = atributo;
                this.attributes.remove(labelColumn);
            }
        });
    }

    public boolean isFirstLineAttribute() {
        return firstLineAttribute;
    }

    public void setFirstLineAttribute(boolean firstLineAttribute) {
        this.firstLineAttribute = firstLineAttribute;
    }

    private void addLineAttributeOrigin(String line) {
        String[] split = line.split(this.delimiter);
        int pos = 0;
        for (String valor : split) {
            attributesOrigin.add(new Attribute(pos++, this.firstLineAttribute ? valor : String.format("Attrib %d", pos)));
        }
    }

    private void addLineAttributeInstance(long pos, String line) {
        if (line != null && line.trim().length() > 0) {
            String[] split = line.split(this.delimiter);
            int posCol = 0;
            int posColReal = 0;
            Register register = new Register(pos);
            for (String valor : split) {
                if (!this.discardedColumns.contains(posColReal)) {
                    if (this.columnLabel == posColReal) {
                        Label labelTemp = new Label(valor,this.labelInstancePositive);
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
    }

    public void removeAttributesPos(List<Integer> posAttribRemove) {
        List<Attribute> attributesRemove = new ArrayList<>();
        posAttribRemove.forEach(value -> {
            attributesRemove.add(new Attribute(value, "temp"));
        });
        this.removeAttributes(attributesRemove);
    }

    public void removeAttributes(List<Attribute> attributesRemove) {
        this.attributes.clear();
        this.attributesOrigin.forEach(attribute -> {
            if (!attributesRemove.contains(attribute)) {
                this.attributes.add(attribute);
            } else {
                this.discardedColumns.add(attribute.getPosition());
            }
        });
    }

    public void process(BufferedReader arquivo) throws IOException {
        String linha = arquivo.readLine();
        boolean firstLine = true;
        long pos = 0;
        this.registers.clear();
        while (linha != null) {
            if (firstLine) {
                firstLine = false;
            } else {
                this.addLineAttributeInstance(pos, linha);
            }
            linha = arquivo.readLine();
            pos++;
        }
    }

    public void avaliaFirstLine(BufferedReader arquivo) throws IOException {
        this.attributesOrigin = new ArrayList<>();
        String linha = arquivo.readLine();
        long pos = 0;
        if (linha != null) {
            this.addLineAttributeOrigin(linha);
        }
    }

}

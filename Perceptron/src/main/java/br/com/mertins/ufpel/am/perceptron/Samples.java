package br.com.mertins.ufpel.am.perceptron;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.ElementValue;
import br.com.mertins.ufpel.am.preparacao.Label;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
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
public class Samples implements Serializable {

    private String delimiter = ",";
    private List<Attribute> attributesOrigin = new ArrayList<>();
    private final List<ElementValue> attributes = new ArrayList<>();
    private final List<Integer> discardedColumns = new ArrayList<>();
    private final Set<Label> labels = new HashSet<>();
    private int columnLabel;
    private Attribute labelColumn;
    private CSVReader csvReader;
    private boolean csvFirstLine = true;
    private String fileName;
    private FunctionSampleOut function;

    private SamplesParameters parameters;

    public Samples(SamplesParameters parameters,FunctionSampleOut function) {
        this.parameters = parameters;
        this.function=function;
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

    public void defineColumnLabel(int columnLabel) {
        this.columnLabel = columnLabel;
        this.getAttributesOrigin().forEach(atributo -> {
            if (atributo.getPosition() == columnLabel) {
                labelColumn = atributo;
                this.attributes.remove(labelColumn);
            }
        });
    }

    public void notRemoveAttributes() {
        this.removeAttributes(new ArrayList<>());
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
        this.discardedColumns.clear();
        this.attributesOrigin.forEach(attribute -> {
            if (!attributesRemove.contains(attribute)) {
                this.attributes.add(attribute);
            } else {
                this.discardedColumns.add(attribute.getPosition());
            }
        });
    }

    public void avaliaFirstLine(String filename) throws IOException {
        this.avaliaFirstLine(new File(filename));
    }

    public void avaliaFirstLine(File file) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(file), this.delimiter.charAt(0))) {
            String[] colunas;
            if ((colunas = reader.readNext()) != null) {
                this.attributesOrigin = new ArrayList<>();
                int pos = 0;
                for (String valor : colunas) {
                    attributesOrigin.add(new Attribute(pos++, this.parameters.isFirstLineAttribute() ? valor : String.format("Attrib %d", pos)));
                }
            }
        }
        this.defineColumnLabel(parameters.getColumnLabel());
    }

    public void open(String filename) throws IOException {
        this.open(new File(filename));
    }

    public void open(File file) throws IOException {
        this.fileName = file.getAbsolutePath();
        csvReader = new CSVReader(new FileReader(file), this.delimiter.charAt(0));
        csvFirstLine = true;
    }

    public Sample next() throws IOException {
        String[] colunas = csvReader.readNext();
        if (this.parameters.isFirstLineAttribute() && this.csvFirstLine) {
            colunas = csvReader.readNext();
        }
        this.csvFirstLine = false;
        if (colunas != null && colunas.length == this.attributesOrigin.size()) {
            Sample sample = new Sample(this.parameters.isNormalize());
            int pos = 0;
            for (String value : colunas) {
                if (pos == this.columnLabel) {
                    this.function.prepare(value, sample);
                } else {
                    // no futuro colocar aqui código para remover as colunas desnecessárias similar ao que ocorre no id3. Atualmente não esta levando em conta as informações do discardedColumns
                    sample.addIn(Integer.valueOf(value));
                }
                pos++;
            }
            return sample;
        }
        return null;
    }

    public void close() throws IOException {
        csvReader.close();
    }

    public void reset() throws IOException {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.open(fileName);
    }
    
    public int amountAttibutes(){
        return this.attributes.size()-1;
    }
}

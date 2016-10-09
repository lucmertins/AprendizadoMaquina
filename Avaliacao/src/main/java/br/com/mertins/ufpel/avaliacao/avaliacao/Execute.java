package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Sample;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Execute {

    private List<Attribute> atributos;

    public static void main(String[] args) {
        try {
            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            System.out.printf("Arquivo %s\n", file.getAbsolutePath());
            SamplePerceptron sample = new SamplePerceptron();
            sample.setFirstLineAttribute(false);
            sample.avaliaFirstLine(file);
            sample.defineColumnLabel(0);
            List<Integer> remove = new ArrayList<>();
            sample.removeAttributesPos(remove);
            
            sample.process(file);
            System.out.println("foi");
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }
}

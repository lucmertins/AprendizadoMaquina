/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.am.preparacao.Sample;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Execute {

    private List<Attribute> atributos;

//    public void avaliaFirstLine(File file) throws IOException {
//        try (CSVReader reader = new CSVReader(new FileReader(file), this.delimiter.charAt(0))) {
//            String[] colunas;
//            if ((colunas = reader.readNext()) != null) {
//                this.atributos = new ArrayList<>();
//                int pos = 0;
//                for (String valor : colunas) {
//                    atributos.add(new Attribute(pos++, String.format("Attrib %d", pos)));
//                }
//            }
//        }
//    }
    public static void main(String[] args) {
        try {
            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            System.out.printf("Arquivo %s\n", file.getAbsolutePath());
            Sample sample = new Sample();
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

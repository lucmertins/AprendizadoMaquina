package br.com.mertins.ufpel.am.preparacao;

import br.com.mertins.ufpel.am.id3.ID3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void main(String[] args) {
        try {
            Sample sample = new Sample(5);
            sample.addDiscardedColumns(0);
            try (FileReader arq = new FileReader("beach.csv")) {
                BufferedReader lerArq = new BufferedReader(arq);
                sample.process(lerArq);
            }
            System.out.println("******");
            ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes(), sample.getLabels());
            id3.process();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }
}

package br.com.mertins.ufpel.am.preparacao;

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
            Sample sample = new Sample();
            try (FileReader arq = new FileReader("beach.csv")) {
                BufferedReader lerArq = new BufferedReader(arq);
                String linha = lerArq.readLine();
                boolean firstLine = true;
                long pos = 0;
                while (linha != null) {
                    System.out.printf("%s\n", linha);
                    if (firstLine) {
                        sample.addLineAttribute(linha);
                        firstLine = false;
                    } else {
                        sample.addLineAttributeInstance(pos, linha);
                    }
                    linha = lerArq.readLine();
                    pos++;
                }
            }

            System.out.println("******");
            sample.getAttributes().stream().forEach((attribute) -> {
                System.out.println(attribute.toString());
            });
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }
}

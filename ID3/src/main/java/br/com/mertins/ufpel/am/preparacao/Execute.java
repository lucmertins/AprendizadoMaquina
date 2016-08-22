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
            Sample sample = new Sample(5);
            sample.addDiscardedColumns(0);
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
                attribute.getAttributesInstance().stream().forEach((attributeInstance) -> {
                    System.out.printf("\t\t%s\n", attributeInstance.toString());
                });

            });
            System.out.println("******");
            sample.getRotulos().stream().forEach((rotulo) -> {
                System.out.printf("%s\n", rotulo.toString());
            });
            System.out.println("******");

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }
}

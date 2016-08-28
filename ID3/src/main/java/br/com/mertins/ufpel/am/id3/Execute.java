package br.com.mertins.ufpel.am.id3;

import br.com.mertins.ufpel.am.preparacao.Sample;
import br.com.mertins.ufpel.am.tree.Node;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void main(String[] args) {
        String fileName="beach.csv";
        if (args.length == 1) {
            fileName=args[0];
           
        }
        try {
            File file=new File(fileName);
            System.out.printf("Arquivo %s\n",file.getAbsolutePath());
            Sample sample = new Sample(5);
            sample.addDiscardedColumns(0);
            try (FileReader arq = new FileReader(fileName)) {
                BufferedReader lerArq = new BufferedReader(arq);
                sample.process(lerArq);
            }
            System.out.println("******");
            ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes());
            Node root = id3.process();
            root.print();
            System.out.println("******");
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

    }
}

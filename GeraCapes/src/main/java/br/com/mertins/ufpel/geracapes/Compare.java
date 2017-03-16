package br.com.mertins.ufpel.geracapes;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class Compare {

    public void run(Properties properties) throws IOException {
        String file1 = (String) properties.get("file1");
        String file2 = (String) properties.get("file2");
        boolean first = Boolean.parseBoolean(((String) properties.get("firstlineattribute")).trim());
        double iguais = 0, diferente = 0;
        try (CSVReader reader1 = new CSVReader(new FileReader(file1), ',')) {
            try (CSVReader reader2 = new CSVReader(new FileReader(file2), ',')) {
                String[] colunasReader1, colunasReader2;

                while ((colunasReader1 = reader1.readNext()) != null) {
                    colunasReader2 = reader2.readNext();
                    if (first) {
                        first = false;
                    } else {
                        int last = colunasReader1.length - 1;
                        boolean equals = colunasReader1[last].equals(colunasReader2[last]);
//                        System.out.printf("%s %s %s %s %b\n", colunasReader1[0], colunasReader2[0], colunasReader1[last], colunasReader2[last],equals);
                        if (equals) {
                            iguais++;
                        } else {
                            diferente++;
                        }
                    }
                }
            }
        }
        double concord = iguais * 100 / (iguais + diferente);
        double discord = diferente * 100 / (iguais + diferente);
        System.out.printf("Arquivos concordam em %f avaliações e discordar em %f avaliações\n", iguais, diferente);
        System.out.printf("%f%% concordando e %f%% discornando\n", concord, discord);
    }
}

package br.com.mertins.ufpel.geracapes;

import com.opencsv.CSVReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<String> linhasReagrupadas = new ArrayList<>();
        try (CSVReader reader1 = new CSVReader(new FileReader(file1), ',')) {
            try (CSVReader reader2 = new CSVReader(new FileReader(file2), ',')) {
                String[] colunasReader1, colunasReader2;

                while ((colunasReader1 = reader1.readNext()) != null) {
                    colunasReader2 = reader2.readNext();
                    StringBuilder linha = new StringBuilder();
                    if (first) {
                        first = false;
                        remontaLinha(linha, colunasReader1);
                    } else {
                        int last = colunasReader1.length - 1;
                        boolean equals = colunasReader1[last].equals(colunasReader2[last]);
//                        System.out.printf("%s %s %s %s %b\n", colunasReader1[0], colunasReader2[0], colunasReader1[last], colunasReader2[last],equals);
                        if (equals) {
                            iguais++;
                            remontaLinha(linha, colunasReader1);
                        } else {
                            diferente++;
                            if (colunasReader2[last].equalsIgnoreCase("Tres") || (colunasReader2[last].equalsIgnoreCase("Quatro"))) {
                                remontaLinha(linha, colunasReader2);
                            } else {
                                remontaLinha(linha, colunasReader1);
                            }
                        }
                    }
                    linhasReagrupadas.add(linha.toString());
                }
            }
        }
        String property = System.getProperty("user.home");
        try (BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(String.format("%s%stemp%sCapsResultMesclado.csv", property, File.separator, File.separator)))) {
            for (String linha : linhasReagrupadas) {
                bufferWrite.write(linha);
                bufferWrite.write("\n");
            }
        }

        double concord = iguais * 100 / (iguais + diferente);
        double discord = diferente * 100 / (iguais + diferente);
        System.out.printf("Arquivos concordam em %f avaliações e discordam em %f avaliações\n", iguais, diferente);
        System.out.printf("%f%% iguais e %f%% diferentes\n", concord, discord);
    }

    private void remontaLinha(StringBuilder linha, String[] values) {
        for (String col : values) {
            linha.append(col);
            linha.append(",");
        }
        linha.deleteCharAt(linha.length() - 1);
    }
}

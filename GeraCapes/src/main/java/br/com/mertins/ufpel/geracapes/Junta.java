package br.com.mertins.ufpel.geracapes;

import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class Junta {

    public void run(Properties properties) throws IOException {
        String property = System.getProperty("user.home");

        try (CSVReader reader1 = new CSVReader(new FileReader(String.format("%s%stemp%sCapsResultMesclado.csv", property, File.separator, File.separator)), ',')) {
            try (BufferedReader bufferRead = new BufferedReader(new FileReader(String.format("%s%stemp%scapes_teste_a_preencher.csv", property, File.separator, File.separator)))) {
                try (BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(String.format("%s%stemp%scapes_teste_preenchido.csv", property, File.separator, File.separator)))) {
                    String[] colunasReader1;
                    boolean firstLine = true;
                    while ((colunasReader1 = reader1.readNext()) != null) {
                        String linha = bufferRead.readLine();
                        if (!firstLine) {
                            bufferWrite.write(linha);
                            bufferWrite.write(colunasReader1[colunasReader1.length - 1]);
                            bufferWrite.write("\n");
                        } else {
                            firstLine = false;
                            bufferWrite.write(linha);
                            bufferWrite.write("\n");
                        }
                    }
                }
            }
        }
//        
//        try (BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(String.format("%s%stemp%sCapsResultMesclado.csv", property, File.separator, File.separator)))) {
//            for (String linha : linhasReagrupadas) {
//                bufferWrite.write(linha);
//                bufferWrite.write("\n");
//            }
//        }
    }
}

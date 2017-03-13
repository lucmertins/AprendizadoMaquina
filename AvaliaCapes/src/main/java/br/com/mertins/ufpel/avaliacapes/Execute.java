package br.com.mertins.ufpel.avaliacapes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class Execute {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String action = "trainer";
        String filename = "/home/mertins/Desenvolvimento/Java/UFPel/FIA/AprendizadoMaquina/AvaliaCapes/fileTrainerMLPCapes.config";
//        if (args.length == 3) {
//            action = args[1].toLowerCase().trim();
//            filename = args[2];
        File file = new File(filename);
        if (file.exists() && file.isFile() && avalAction(action)) {
            System.out.printf("Ação [%s]. Processando arquivo [%s]\n", action, file.getAbsoluteFile());
            Properties properties = new Properties();
            properties.load(new FileInputStream(filename));

            PreparaArquivo preparaArquivo = new PreparaArquivo();
            preparaArquivo.exec(properties);

//            ExecuteMLP execMLP = new ExecuteMLP();
//            switch (action) {
//                case "trainer":
//                    
//                    execMLP.training(properties);
//                    break;
//                case "eval":
//                    execMLP.evaluation(properties);
//                    break;
//            }
//            } else {
//                msgOut(action, file);
//            }
        }
    }

    private static boolean avalAction(String action) {
        return "trainer".equalsIgnoreCase(action.trim()) || "eval".equalsIgnoreCase(action.trim());
    }

    private static void msgOut(String action, File file) {
        System.out.printf("Ação [%s] não foi realizada. Ação ou Arquivo [%s] não encontrado\n", action, file == null ? "" : file.getAbsoluteFile());
        System.out.println("Ações possíveis: TRAINER EVAL");

    }

}

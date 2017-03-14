package br.com.mertins.ufpel.avaliacapes;

import br.com.mertins.ufpel.avaliacao.knn.ExecuteKNN;
import br.com.mertins.ufpel.avaliacao.redeneural.ExecuteMLP;
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
        String action = "knn";
        String filename = "/home/mertins/Desenvolvimento/Java/UFPel/FIA/AprendizadoMaquina/AvaliaCapes/fileTrainerKnnCapes1.config";
//        if (args.length == 2) {
//            action = args[0].toLowerCase().trim();
//            filename = args[1];
            File file = new File(filename);
            if (file.exists() && file.isFile() && avalAction(action)) {
                System.out.printf("Ação [%s]. Processando arquivo [%s]\n", action, file.getAbsoluteFile());
                Properties properties = new Properties();
                properties.load(new FileInputStream(filename));
                switch (action) {
                    case "prepar":
                        PreparaArquivo preparaArquivo = new PreparaArquivo();
                        preparaArquivo.exec(properties);
                        break;
                    case "trainer":
                        ExecuteMLP execMLPt = new ExecuteMLP();
                        execMLPt.training(properties);
                        break;
                    case "eval":
                        ExecuteMLP execMLPe = new ExecuteMLP();
                        execMLPe.evaluation(properties);
                        break;
                    case "knn":
                        ExecuteKNN execKnn = new ExecuteKNN();
                        execKnn.evaluation(properties);
                        break;
                }
            } else {
                msgOut(action, file);
            }
//        } else {
//            msgOut(action, null);
//        }
    }

    private static boolean avalAction(String action) {
        return "trainer".equalsIgnoreCase(action.trim())
                || "eval".equalsIgnoreCase(action.trim())
                || "prepar".equalsIgnoreCase(action.trim())
                || "knn".equalsIgnoreCase(action.trim());
    }

    private static void msgOut(String action, File file) {
        System.out.printf("Ação [%s] não foi realizada. Ação ou Arquivo [%s] não encontrado\n", action, file == null ? "" : file.getAbsoluteFile());
        System.out.println("Ações possíveis: PREPAR TRAINER EVAL KNN");

    }

}

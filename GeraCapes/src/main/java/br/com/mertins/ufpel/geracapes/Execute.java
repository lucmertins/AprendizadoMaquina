package br.com.mertins.ufpel.geracapes;

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
        String resource = "knn";
        String filename = "/home/mertins/Desenvolvimento/Java/UFPel/FIA/AprendizadoMaquina/GeraCapes/fileGeraCapesKnn.config";
//        if (args.length == 2) {
//            resource = args[0].toLowerCase().trim();
//            filename = args[1];
            File file = new File(filename);
            if (file.exists() && file.isFile() && avalResource(resource)) {
                System.out.printf("Estratégia [%s]. Processando arquivo [%s]\n", resource, file.getAbsoluteFile());
                Properties properties = new Properties();
                properties.load(new FileInputStream(filename));
                switch (resource) {
                    case "prepar":
                        PreparaArquivo preparaArquivo = new PreparaArquivo();
                        preparaArquivo.exec(properties);
                        break;
                    case "mlp":
                        
                        break;
                    case "knn":
                        GeraKnn geraKnn=new GeraKnn();
                        geraKnn.run(properties);
                        break;
                }

            } else {
                msgOut(resource, file);
            }
//        } else {
//            msgOut(resource, null);
//        }
    }

    private static boolean avalResource(String resource) {
        return "mlp".equalsIgnoreCase(resource.trim())
                || "knn".equalsIgnoreCase(resource.trim())
                || "prepar".equalsIgnoreCase(resource.trim());
    }

    private static void msgOut(String resource, File file) {
        System.out.printf("Estratégia [%s]. Estratégia ou Arquivo [%s] não encontrado\n", resource, file == null ? "" : file.getAbsoluteFile());
        System.out.println("Estratégias possíveis: PREPAR MLP KNN");

    }
}

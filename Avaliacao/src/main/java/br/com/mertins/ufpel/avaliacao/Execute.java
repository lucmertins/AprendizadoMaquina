package br.com.mertins.ufpel.avaliacao;

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
        String resource;
        String action;
        String filename;
        if (args.length < 3) {
            resource = "mlp";
            action = "trainer";
            filename = "linuxTrainer.config";
        } else {
            resource = args[0];
            action = args[1];
            filename = args[2];
        }
        File file = new File(filename);
        if (file.exists() && file.isFile() && avalResource(resource) && avalAction(action)) {
            System.out.printf("Estratégia [%s]. Ação [%s]. Processando arquivo [%s]\n", resource, action, file.getAbsoluteFile());
            Properties properties = new Properties();
            properties.load(new FileInputStream(filename));
            switch (resource) {
                case "mlp":
                    ExecuteMLP exec = new ExecuteMLP();
                    exec.training(properties);
                    break;
            }
        } else {
            System.out.printf("Estratégia [%s]. Ação [%s] não foi realizada. Arquivo [%s] não encontrado\n", resource, action, file.getAbsoluteFile());
        }
    }

    private static boolean avalAction(String action) {
        return "trainer".equalsIgnoreCase(action.trim());
    }

    private static boolean avalResource(String action) {
        return "mlp".equalsIgnoreCase(action.trim());
    }

}

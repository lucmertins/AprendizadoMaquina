package br.com.mertins.ufpel.avaliacao;

import br.com.mertins.ufpel.avaliacao.perceptron.ExecutePerceptron;
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
            resource = "perceptron";
            action = "eval";
            filename = "fileTrainerPerceptron.config";
        } else {
            resource = args[0].toLowerCase().trim();
            action = args[1].toLowerCase().trim();
            filename = args[2];
        }
        File file = new File(filename);
        if (file.exists() && file.isFile() && avalResource(resource) && avalAction(action)) {
            System.out.printf("Estratégia [%s]. Ação [%s]. Processando arquivo [%s]\n", resource, action, file.getAbsoluteFile());
            Properties properties = new Properties();
            properties.load(new FileInputStream(filename));
            switch (resource) {
                case "mlp":
                    ExecuteMLP execMLP = new ExecuteMLP();
                    switch (action) {
                        case "trainer":
                            execMLP.training(properties);
                            break;
                        case "eval":
                            execMLP.evaluation(properties);
                            break;
                    }
                    break;
                case "perceptron":
                    ExecutePerceptron execP = new ExecutePerceptron();
                    switch (action) {
                        case "trainer":
                            execP.training(properties);
                            break;
                        case "eval":
                            execP.evaluation(properties);
                            break;
                    }
                    break;
            }
        } else {
            System.out.printf("Estratégia [%s]. Ação [%s] não foi realizada. Estratégia ou Ação ou Arquivo [%s] não encontrado\n", resource, action, file.getAbsoluteFile());
            System.out.println("Estratégias possíveis: PERCEPTRON MLP");
            System.out.println("Ações possíveis: TRAINER EVAL");
        }
    }

    private static boolean avalAction(String action) {
        return "trainer".equalsIgnoreCase(action.trim()) || "eval".equalsIgnoreCase(action.trim());
    }

    private static boolean avalResource(String action) {
        return "mlp".equalsIgnoreCase(action.trim()) || "perceptron".equalsIgnoreCase(action.trim());
    }

}

package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecuteAvaliacao {

    public void testar() throws IOException, ClassNotFoundException {
        File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
        System.out.printf("Arquivo %s\n", file.getAbsolutePath());
        Samples samples = new Samples();
        samples.setNegativeValue(0);
        samples.setPositiveValue(1);
        samples.setFirstLineAttribute(false);
        samples.avaliaFirstLine(file);
        samples.defineColumnLabel(0);
        samples.open(file);
        Sample sample;

        Perceptron perceptron = Perceptron.deserialize("/home/mertins/Documentos/tmp/perceptronZero_3.obj");
        double truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
        while ((sample = samples.next()) != null) {
            perceptron.fill(sample);
            perceptron.setAlgorithm(Perceptron.AlgorithmSimoid.HARD_0);
            double out = perceptron.out();

            if (sample.getValue() == 0) {
                if (out == 1) {
                    System.out.printf("True Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                    truePositive++;
                } else {
                    System.out.printf("False Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                    falseNegative++;
                }
            } else if (out == 1) {
                System.out.printf("False Positive Sample [%f]   out [%f]\n", sample.getValue(), out);
                falsePositive++;
            } else {
                System.out.printf("True Negative Sample [%f]   out [%f]\n", sample.getValue(), out);
                trueNegative++;
            }
        }
        samples.close();
        double accuracia = (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
        System.out.printf("truePositive [%f] trueNegative [%f] falsePositive [%f] falseNegative [%f]\n", truePositive, trueNegative, falsePositive, falseNegative);
        System.out.printf("Acuracia [%f]\n", accuracia);

    }

//     private FileWriter arquivo(Conexao jog1, Conexao jog2) {
//        String property = System.getProperty("user.home");
//        File folder = new File(String.format("%s%sIAJungleWork", property, File.separator));
//        if (!folder.exists()) {
//            folder.mkdir();
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        String nome = String.format("%s%s%s%d%d.log", folder.getAbsolutePath(), File.separator, sdf.format(new Date()), jog1.getPort(), jog2.getPort());
//        try {
//            return new FileWriter(nome);
//        } catch (IOException ex) {
//            Logger.getLogger(ExecuteServer.class.getName()).log(Level.SEVERE, "Não criou o arquivo", ex);
//            return null;
//        }
//    }
}

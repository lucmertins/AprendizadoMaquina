package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.Training;
import br.com.mertins.ufpel.am.preparacao.Attribute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Execute {

    
    
    public static void main(String[] args) {
        try {
            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
            System.out.printf("Arquivo %s\n", file.getAbsolutePath());
            Samples samples = new Samples();
            samples.setFirstLineAttribute(false);
            samples.avaliaFirstLine(file);
            samples.defineColumnLabel(0);
            List<Integer> remove = new ArrayList<>();
            samples.removeAttributesPos(remove);
            samples.open(file);
            samples.setTruePositive("8");
            Training training=new Training();
            Perceptron perceptronZero = training.withDelta(samples,  0.00001, 5,Perceptron.AlgorithmSimoid.TANGEN);
            
            
            samples.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }

    
//    public static void main(String[] args) {
//        try {
//            File file = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
//            System.out.printf("Arquivo %s\n", file.getAbsolutePath());
//            Samples samples = new Samples();
//            samples.setFirstLineAttribute(false);
//            samples.avaliaFirstLine(file);
//            samples.defineColumnLabel(0);
//            List<Integer> remove = new ArrayList<>();
//            samples.removeAttributesPos(remove);
//            samples.open(file);
//            Sample sample;
//            int linha=0;
//            while ((sample = samples.next()) != null && linha++<3) {
//                System.out.printf("%s %d %d\n", sample.getValue(), sample.amountIn(), sample.getIn(1));
//                sample.getIns().forEach(value -> {
//                    System.out.printf("%d ", value);
//                });
//                System.out.println();
//            }
//            System.out.println("Reset");
//            samples.reset();
//            linha=0;
//            while ((sample = samples.next()) != null && linha++<3) {
//                System.out.printf("%s %d %d\n", sample.getValue(), sample.amountIn(), sample.getIn(1));
//                sample.getIns().forEach(value -> {
//                    System.out.printf("%d ", value);
//                });
//                System.out.println();
//            }
//            samples.close();
//        } catch (IOException e) {
//            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
//        }
//    }
}

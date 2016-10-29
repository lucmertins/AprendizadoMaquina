package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author mertins
 */
public class DefineNumero {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String path = "/home/mertins/IAPerceptron/20161014_074950";
//        String path = "/Users/mertins/IAPerceptron/20161014_085630";
        String versao = "10";
         File fileTest = new File("/home/mertins/Documentos/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");
//        File fileTest = new File("/Users/mertins/Documents/UFPel/Dr/AprendizadoMaquina/mnist/mnist_test.csv");

        Perceptron[] perceptrons = new Perceptron[10];

        for (int i = 0; i < 10; i++) {
            String arquivo = String.format("%s%sperceptron_%d_%s", path, File.separator, i, versao);
            Perceptron perceptron = Perceptron.deserialize(arquivo);
            perceptron.setAlgorithm(Perceptron.AlgorithmSimoid.LOGISTIC);
            perceptrons[i] = perceptron;
        }

        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(true);   // transforme atributos em 0 ou 1
//        parameters.setNegativeValue(0);
//        parameters.setPositiveValue(1);
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
        Samples samples = new Samples(parameters,new FunctionSampleOut() {
            @Override
            public void prepare(String value, Sample sample) {
              sample.addOut(value.equals("0")?0:1);                     // rever o código pois esta inadequado para testar perceptrons individualmente
            }
        });
        Sample sample;
        samples.avaliaFirstLine(fileTest);
        samples.open(fileTest);
        double acertou = 0, errou = 0;
        while ((sample = samples.next()) != null) {
            double melhorValor = -1;
            int percEscolhido = -1;
            int perc = 0;
            for (int i = 0; i < 10; i++) {
                perceptrons[i].fill(sample);
                double out = perceptrons[i].out();
                if (out > melhorValor) {
                    percEscolhido = perc;
                    melhorValor = out;
                }
                perc++;
            }

//            System.out.printf("Sample [%f]   out [%d]\n", sample.getValue(), percEscolhido);
            if (sample.getOut(1)== percEscolhido) {
                acertou++;
            } else {
                errou++;
            }

        }
        System.out.printf("Acertou [%.0f %.3f%%]   Errou [%.0f %.3f%%] \n", acertou,acertou/(acertou+errou)*100, errou,errou/(acertou+errou)*100);

    }
}

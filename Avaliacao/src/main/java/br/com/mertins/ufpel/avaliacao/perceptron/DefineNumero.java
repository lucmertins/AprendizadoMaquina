package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import java.io.IOException;

/**
 *
 * @author mertins
 */
public class DefineNumero {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String[] arquivos = {
            "/home/mertins/IAPerceptron/20161013_203249/perceptron_0_13", "/home/mertins/IAPerceptron/20161013_203249/perceptron_1_13",
            "/home/mertins/IAPerceptron/20161013_203249/perceptron_2_13", "/home/mertins/IAPerceptron/20161013_203249/perceptron_3_13",
            "/home/mertins/IAPerceptron/20161013_203249/perceptron_4_13", "/home/mertins/IAPerceptron/20161013_203249/perceptron_5_13",
            "/home/mertins/IAPerceptron/20161013_203249/perceptron_6_13", "/home/mertins/IAPerceptron/20161013_203249/perceptron_7_13",
            "/home/mertins/IAPerceptron/20161013_203249/perceptron_7_13", "/home/mertins/IAPerceptron/20161013_203249/perceptron_8_13"
        };

        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(true);   // transforme atributos em 0 ou 1
        parameters.setNegativeValue(0);
        parameters.setPositiveValue(1);
        parameters.setFirstLineAttribute(false);
        parameters.setColumnLabel(0);
        Samples samples = new Samples(parameters);
        Sample sample;

        while ((sample = samples.next()) != null) {
            double melhorValor = -1;
            int percEscolhido = -1;
            int perc = 0;
            for (String arquivo : arquivos) {
                Perceptron perceptron = Perceptron.deserialize(arquivo);
                perceptron.setAlgorithm(Perceptron.AlgorithmSimoid.LOGISTIC);
                perceptron.fill(sample);
                double out = perceptron.out();
                if (out > melhorValor) {
                    percEscolhido = perc;
                    melhorValor = out;
                }
                perc++;
            }
            System.out.printf("Sample [%f]   out [%d]\n", sample.getValue(), percEscolhido);

        }

    }
}

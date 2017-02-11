package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.OutPerceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author mertins
 */
public class ExecuteAvaliacao {

    public ExecuteAvaliacao() {
    }

    public Accumulator[] run(File fileTest, SamplesParameters samplesParameters, String fileMLP) throws IOException, ClassNotFoundException {
        Samples samples = new Samples(samplesParameters, new FunctionSampleOut() {
            @Override
            public void prepare(String value, Sample sample) {
                for (int i = 0; i < 10; i++) {
                    sample.addOut(Integer.valueOf(value) == i ? 1 : 0);
                }
                sample.addOutOriginal(value);
            }
        });
        samples.avaliaFirstLine(fileTest);
        samples.open(fileTest);
        Sample sample;
        MLP rede = MLP.deserialize(fileMLP);
        Accumulator[] acumuladores = new Accumulator[10];
        for (int i = 0; i < 10; i++) {
            acumuladores[i] = new Accumulator(i);
        }
        int pos = 0;
        while ((sample = samples.next()) != null) {
            rede.updateIn(sample);
            OutPerceptron[] outs = rede.process();
            int saidaEscolhida = -1;
            double melhorValor = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < outs.length; i++) {
                if (outs[i].getOut() > melhorValor) {
                    melhorValor = outs[i].getOut();
                    saidaEscolhida = i;
                }
            }
            int saidaCorreta = Integer.parseInt(sample.getOutOriginal(1));
            if (saidaEscolhida == saidaCorreta) {
                // verdadeiro positivo para saidaescolhida
//                System.out.printf("True Positive Sample [%d]   out [%d]\n", saidaCorreta, saidaEscolhida);
                acumuladores[saidaEscolhida].addTruePositive();
                // verdadeiro negativo para todos os outros labels
                for (int i = 0; i < 10; i++) {
                    if (i != saidaEscolhida) {
                        acumuladores[i].addTrueNegative();
                    }
                }
            } else {
//                System.out.printf("False Positive Sample [%d]   out [%d]\n", saidaCorreta, saidaEscolhida);
                // falso positivo para o saidaEscolhida
                acumuladores[saidaEscolhida].addFalsePositive(saidaCorreta);

                // falso negativo para o valor original
                acumuladores[saidaCorreta].addFalseNegative(saidaEscolhida);

                // verdadeiro negativo para todos os outros labels 
                for (int i = 0; i < 10; i++) {
                    if (i != saidaEscolhida && i != saidaCorreta) {
                        acumuladores[i].addTrueNegative();
                    }
                }

            }
        }
        samples.close();
        return acumuladores;
    }
}

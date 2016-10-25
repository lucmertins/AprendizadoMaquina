package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 *
 * @author mertins
 */
public class Training {

    private boolean blocbkIfBadErr = true;

    public Training(boolean blocbkIfBadErr) {
        this.blocbkIfBadErr = blocbkIfBadErr;
    }

    private ObservatorTraining observator = new ObservatorTraining() {
    };

    public void withBackPropagation(MLP rede, List<Sample> samples, double learningRate, int epoca) {

        for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
            for (Sample sample : samples) {
                rede.updateIn(sample);
                double[] outs = rede.process();
                double[] sigmasOut = new double[rede.amountOut()];
                for (int i = 0; i < sigmasOut.length; i++) {
                    double err = sample.getOut(i + 1) - outs[i];
                    sigmasOut[i] = outs[i] * (1 - outs[i]) * err;
                    System.out.printf("Erro [%.30f]    Sigma [%f.30]\n", err, sigmasOut[i]);
                }
                System.out.printf("Exemplo in[%s] out[%s] \n", sample.toStringIn(), sample.toStringOut());

                for (int i = rede.amountHiddenLayer(); i > 0; i--) {
                    Layer hiddenLayer = rede.getLayer(i);
                    double[] sigmasHidden = new double[hiddenLayer.amount()];
                    double[] outsHidden = hiddenLayer.getOuts();
                    for (int h = 0; h < hiddenLayer.amount(); h++) {
                        sigmasHidden[h] = outsHidden[h] * (1 - outsHidden[h]) * somatorio(h+1, sigmasOut, rede.getOuts());
                    }
                }
            }
            System.out.printf("Epoca Out [%d]\n", epocaTemp);
        }
    }

    private double somatorio(int positonWeigth, double[] sigmas, List<Perceptron> perceptrons) {
        int pos = 0;
        double totalizador = 0;
        for (Perceptron perceptron : perceptrons) {
            totalizador += perceptron.weigth(positonWeigth) * sigmas[pos];
            pos++;
        }
        return totalizador;
    }

    public MLP withBackPropagation(Samples samples, double learningRate, double moment, int epoca, FileWriter out) throws IOException, ClassNotFoundException {
        return null;
    }

    public void addListenerObservatorTraining(ObservatorTraining observator) {
        this.observator = observator;
    }

    private void register(Instant inicio, int epoca, double errEpoca) {
        Duration duration = Duration.between(inicio, Instant.now());
        this.observator.register(duration, epoca, errEpoca);
    }
}

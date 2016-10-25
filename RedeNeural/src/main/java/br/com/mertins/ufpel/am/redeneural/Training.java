package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
                System.out.printf("\nExemplo in[%s] out[%s] \n", sample.toStringIn(), sample.toStringOut());
                rede.updateIn(sample);
                double[] outs = rede.process();
                List<double[]> sigmas = new ArrayList<>();
                sigmas.add(computeSigmasOut(rede, sample, outs));

                List<Perceptron> perceptrons = rede.getOuts();
                for (int i = rede.amountHiddenLayer(); i > 0; i--) {
                    sigmas.add(computeSigmasHidden(rede, i, sigmas.get(sigmas.size() - 1), perceptrons));
                    perceptrons = rede.getLayer(i).getPerceptrons();
                }
                for (double[] sigmaLayers : sigmas) {
                    for (double sigma : sigmaLayers) {
                        System.out.printf("%.30f ", sigma);
                    }
                    System.out.println();
                }
                System.out.println("\nSigmashidden");
            }
            System.out.printf("\nEpoca Out [%d]\n", epocaTemp);
        }
    }

    public MLP withBackPropagation(Samples samples, double learningRate, double moment, int epoca, FileWriter out) throws IOException, ClassNotFoundException {
        return null;
    }

    private double[] computeSigmasOut(MLP rede, Sample sample, double[] outs) {
        double[] sigmasOut = new double[rede.amountOut()];
        for (int i = 0; i < sigmasOut.length; i++) {
            double err = sample.getOut(i + 1) - outs[i];
            sigmasOut[i] = outs[i] * (1 - outs[i]) * err;
            System.out.printf("Erro [%.30f]    Sigma [%f.30]\n", err, sigmasOut[i]);
        }
        System.out.println("Sigmasout");
        for (double sigmas : sigmasOut) {
            System.out.printf("%.30f ", sigmas);
        }
        return sigmasOut;
    }

    private double[] computeSigmasHidden(MLP rede, int layerPosition, double[] sigmas, List<Perceptron> perceptrons) {
        double[] sigmasHidden = null;
        Layer hiddenLayer = rede.getLayer(layerPosition);
        sigmasHidden = new double[hiddenLayer.amount()];
        double[] outsHidden = hiddenLayer.getOuts();
        for (int orderWeigth = 0; orderWeigth < hiddenLayer.amount(); orderWeigth++) {
            sigmasHidden[orderWeigth] = outsHidden[orderWeigth] * (1 - outsHidden[orderWeigth]) * sum(orderWeigth + 1, sigmas, perceptrons);
        }
        return sigmasHidden;
    }

    private double sum(int positonWeigth, double[] sigmas, List<Perceptron> perceptrons) {
        int pos = 0;
        double totalizador = 0;
        for (Perceptron perceptron : perceptrons) {
            totalizador += perceptron.weigth(positonWeigth) * sigmas[pos];
            pos++;
        }
        return totalizador;
    }

    public void addListenerObservatorTraining(ObservatorTraining observator) {
        this.observator = observator;
    }

    private void register(Instant inicio, int epoca, double errEpoca) {
        Duration duration = Duration.between(inicio, Instant.now());
        this.observator.register(duration, epoca, errEpoca);
    }
}

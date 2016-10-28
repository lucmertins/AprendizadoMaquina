package br.com.mertins.ufpel.am.redeneural;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.OutPerceptron;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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

    public void withBackPropagation(MLP rede, List<Sample> samples, double learningRate, double moment, int epoca) {
        for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
            double errEpoca = 0.0;
            for (Sample sample : samples) {
//                System.out.printf("\nExemplo in[%s] out[%s] \n", sample.toStringIn(), sample.toStringOut());
                rede.updateIn(sample);
                OutPerceptron[] outs = rede.process();
                List<double[]> sigmas = new ArrayList<>();
                double[] vSigmasOut = new double[rede.amountOut()];
                double[] errUnitOut = new double[rede.amountOut()];
                for (int i = 0; i < vSigmasOut.length; i++) {
                    double err = sample.getOut(i + 1) - outs[i].getOut();
                    errUnitOut[i] += Math.pow(err, 2);
                    vSigmasOut[i] = outs[i].getOut() * (1 - outs[i].getOut()) * err;
                }
                for (double value : errUnitOut) {
                    errEpoca += value;
                }
                sigmas.add(vSigmasOut);
                List<Perceptron> perceptrons = rede.getOuts();
                for (int i = rede.amountHiddenLayer(); i > 0; i--) {
                    sigmas.add(computeSigmasHidden(rede, i, sigmas.get(sigmas.size() - 1), perceptrons));
                    perceptrons = rede.getLayer(i).getPerceptrons();
                }

                int posLayer = rede.amountHiddenLayer();
                for (int j = 0; j < sigmas.size(); j++) {
                    int pos = 0;
                    perceptrons = j == 0 ? rede.getOuts() : rede.getLayer(posLayer--).getPerceptrons();
                    for (Perceptron perceptron : perceptrons) {
                        double[] sigmasOk = sigmas.get(j);
                        double deltaWeightBias = learningRate * sigmasOk[pos] * perceptron.getBias() + moment * perceptron.getDeltaBiasWeight();
                        perceptron.updateBiasWeightDelta(deltaWeightBias);
                        for (int i = 1; i <= perceptron.amountIn(); i++) {
                            double deltaWeight = learningRate * sigmasOk[pos] * perceptron.in(i) + moment * perceptron.getWeigthDelta(i);
                            perceptron.updateWeightDelta(i, deltaWeight);
                        }
                        pos++;
                    }
                }
            }
            errEpoca = errEpoca / samples.size();
//            System.out.printf("Epoca [%d] Erro [%.30f]\n", epocaTemp, errEpoca);
        }
    }

    public void withBackPropagation(MLP rede, Samples samples, double learningRate, double moment, int epoca, FileWriter out) throws IOException {
        if (samples.amountAttibutes()>0) {
            for (int epocaTemp = 1; epocaTemp <= epoca; epocaTemp++) {
                Instant inicioEpoca = Instant.now();
                double errEpoca = 0.0;
                Sample sample;
                long totalExemplos = 0;
                while ((sample = samples.next()) != null) {
//                System.out.printf("\nExemplo in[%s] out[%s] \n", sample.toStringIn(), sample.toStringOut());
                    totalExemplos++;
                    rede.updateIn(sample);
                    OutPerceptron[] outs = rede.process();
                    List<double[]> sigmas = new ArrayList<>();
                    double[] vSigmasOut = new double[rede.amountOut()];
                    double[] errUnitOut = new double[rede.amountOut()];
                    for (int i = 0; i < vSigmasOut.length; i++) {
                        double err = sample.getOut(i + 1) - outs[i].getOut();
                        errUnitOut[i] += Math.pow(err, 2);
                        vSigmasOut[i] = outs[i].getOut() * (1 - outs[i].getOut()) * err;
                    }
                    for (double value : errUnitOut) {
                        errEpoca += value;
                    }
                    sigmas.add(vSigmasOut);
                    List<Perceptron> perceptrons = rede.getOuts();
                    for (int i = rede.amountHiddenLayer(); i > 0; i--) {
                        sigmas.add(computeSigmasHidden(rede, i, sigmas.get(sigmas.size() - 1), perceptrons));
                        perceptrons = rede.getLayer(i).getPerceptrons();
                    }

                    int posLayer = rede.amountHiddenLayer();
                    for (int j = 0; j < sigmas.size(); j++) {
                        int pos = 0;
                        perceptrons = j == 0 ? rede.getOuts() : rede.getLayer(posLayer--).getPerceptrons();
                        for (Perceptron perceptron : perceptrons) {
                            double[] sigmasOk = sigmas.get(j);
                            double deltaWeightBias = learningRate * sigmasOk[pos] * perceptron.getBias() + moment * perceptron.getDeltaBiasWeight();
                            perceptron.updateBiasWeightDelta(deltaWeightBias);
                            for (int i = 1; i <= perceptron.amountIn(); i++) {
                                double deltaWeight = learningRate * sigmasOk[pos] * perceptron.in(i) + moment * perceptron.getWeigthDelta(i);
                                perceptron.updateWeightDelta(i, deltaWeight);
                            }
                            pos++;
                        }
                    }
                }
                errEpoca = errEpoca / totalExemplos;
                register(inicioEpoca, epocaTemp, errEpoca);
            }
        }
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

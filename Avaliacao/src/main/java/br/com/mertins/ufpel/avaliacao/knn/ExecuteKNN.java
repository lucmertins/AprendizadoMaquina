package br.com.mertins.ufpel.avaliacao.knn;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.redeneural.Accumulator;
import br.com.mertins.ufpel.avaliacao.redeneural.ConfusionMatrix;
import br.com.mertins.ufpel.avaliacao.util.TrainerKNNProperty;
import br.com.mertins.ufpel.knn.Knn;
import br.com.mertins.ufpel.knn.KnnInformation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class ExecuteKNN {

    public void evaluation(Properties properties) throws IOException {
        TrainerKNNProperty propKnn = new TrainerKNNProperty();
        propKnn.setNormalize((String) properties.get("normalize"));
        propKnn.setFirstLineAttribute((String) properties.get("firstlineattribute"));
        propKnn.setColumnLabel((String) properties.get("columnlabel"));
        propKnn.setFileTrainer((String) properties.get("filetrainer"));
        propKnn.setFileTest((String) properties.get("filetest"));
        propKnn.setFileTrainer((String) properties.get("filetrainer"));
        propKnn.setRemoveColumns((String) properties.get("removecolumns"));
        propKnn.setValueK((String) properties.get("valueK"));
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(propKnn.parseNormalize());
        parameters.setFirstLineAttribute(propKnn.parseFirstLineAttribute());
        parameters.setColumnLabel(propKnn.parseColumnLabel());
        parameters.setRemoveColumns(propKnn.parseRemoveColumn());

        Samples samples = new Samples(parameters, new FunctionSampleOut() {
            @Override
            public void prepare(String value, Sample sample) {
                for (int i = 0; i < 5; i++) {
                    sample.addOut(Integer.valueOf(value) == i ? 1 : 0);
                }
                sample.addOutOriginal(value);
            }
        });
        File fileTest = new File(propKnn.getFileTrainer());
        samples.avaliaFirstLine(fileTest);
        samples.open(fileTest);
        Sample cargaSample;
        ArrayList<Sample> exemplos = new ArrayList();
        while ((cargaSample = samples.next()) != null) {
            exemplos.add(cargaSample);
        }
        samples.close();
        Accumulator[] acumuladores = new Accumulator[5];
        for (int i = 0; i < 5; i++) {
            acumuladores[i] = new Accumulator(i);
        }
        for (int posSample = 0; posSample < exemplos.size(); posSample++) {
            Sample sampleAval = exemplos.get(posSample);
            Knn knn = new Knn(sampleAval.getIns().size());
            knn.load(sampleAval);
            int pos = 0;
            List<KnnInformation> lista = new ArrayList<>();
            for (Sample sample : exemplos) {
                if (pos != posSample) {
                    Knn instance = Knn.instanceLabel(sample);
                    double distance = knn.distance(instance);
                    lista.add(new KnnInformation(++pos, distance, sample.getOutOriginal(1)));
                } else {
                    pos++;
                }
            }
            Collections.sort(lista);
            int saidaEscolhida = Knn.dominante(propKnn.parseValueK(), lista);
            int saidaCorreta = Integer.valueOf(sampleAval.getOutOriginal(1));
            if (saidaEscolhida == saidaCorreta) {
                // verdadeiro positivo para saidaescolhida
//                System.out.printf("True Positive Sample [%d]   out [%d]\n", saidaCorreta, saidaEscolhida);
                acumuladores[saidaEscolhida].addTruePositive();
                // verdadeiro negativo para todos os outros labels
                for (int i = 0; i < 5; i++) {
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
                for (int i = 0; i < 5; i++) {
                    if (i != saidaEscolhida && i != saidaCorreta) {
                        acumuladores[i].addTrueNegative();
                    }
                }
            }
//                System.out.printf("Avaliando exemplo pos: %d knn escolheu label %d era %d\n", posSample, saidaEscolhida, saidaCorreta);
//                for (Informacoes inf : lista) {
//                    System.out.printf("sample %d %f %s\n", inf.posicao, inf.distancia, inf.label);
//                }
//                System.out.println("**************************************************");
        }

        ConfusionMatrix confusao = new ConfusionMatrix();
        confusao.resumo(acumuladores, System.out);
        System.out.println();
        confusao.matrix(acumuladores, System.out);
        System.out.printf("\nGerais    Acurácia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n",
                confusao.accuracy(acumuladores), confusao.precision(acumuladores), confusao.recall(acumuladores), confusao.f1(acumuladores));
        System.out.flush();
    }
}

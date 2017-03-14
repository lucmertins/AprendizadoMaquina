package br.com.mertins.ufpel.avaliacao.knn;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.util.TrainerKNNProperty;
import br.com.mertins.ufpel.knn.Knn;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecuteKNN {

    public static Knn knn;

    static {
        double[] atts = {1, 1, 1975, 1975, 45, 109, 119, 593, 415, 10, 27, 89, 78, 97, 42, 72, 111, 67, 1161, 40, 290, 18, 3, 13};
        knn = new Knn(atts.length);
        knn.setAttributes(atts);

    }
//1,USP,1,1975,1975,45,109,119,0.915966387,593,415,10,27,89,78,97,42,72,111,67,1161,40,290,18,3,13,4

    public void evaluation(Properties properties) {
        try {
            TrainerKNNProperty propKnn = new TrainerKNNProperty();
            propKnn.setNormalize((String) properties.get("normalize"));
            propKnn.setFirstLineAttribute((String) properties.get("firstlineattribute"));
            propKnn.setColumnLabel((String) properties.get("columnlabel"));
            propKnn.setFileTrainer((String) properties.get("filetrainer"));
            propKnn.setFileTest((String) properties.get("filetest"));
            propKnn.setFileTrainer((String) properties.get("filetrainer"));
            propKnn.setRemoveColumns((String) properties.get("removecolumns"));

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
            File fileTest = new File(propKnn.getFileTest());
            samples.avaliaFirstLine(fileTest);
            samples.open(fileTest);
            Sample sample;
            while ((sample = samples.next()) != null) {
                Knn.instance(sample);
                System.out.printf("sample %s\n", sample);
            }
//
        } catch (IOException ex) {
            Logger.getLogger(ExecuteKNN.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }
}

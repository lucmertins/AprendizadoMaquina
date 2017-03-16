package br.com.mertins.ufpel.geracapes;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.OutPerceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import br.com.mertins.ufpel.avaliacao.util.TrainerMLPProperty;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class GeraMLP {

    public void run(Properties properties) throws IOException, ClassNotFoundException {
        TrainerMLPProperty propMLP = new TrainerMLPProperty();
        propMLP.setNormalize((String) properties.get("normalize"));
        propMLP.setFirstLineAttribute((String) properties.get("firstlineattribute"));
        propMLP.setColumnLabel((String) properties.get("columnlabel"));
        propMLP.setRemoveColumns((String) properties.get("removecolumns"));
        propMLP.setFileMLP((String) properties.get("filemlp"));
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(propMLP.parseNormalize());
        parameters.setFirstLineAttribute(propMLP.parseFirstLineAttribute());
        parameters.setColumnLabel(propMLP.parseColumnLabel());
        parameters.setRemoveColumns(propMLP.parseRemoveColumn());
        Samples samples = new Samples(parameters, new FunctionSampleOut() {
            @Override
            public void prepare(String value, Sample sample) {
                for (int i = 0; i < 5; i++) {
                    sample.addOut(Integer.valueOf(value) == i ? 1 : 0);
                }
                sample.addOutOriginal(value);
            }
        });
        File fileSamples = new File(propMLP.getFileTrainer());
        samples.avaliaFirstLine(fileSamples);
        samples.open(fileSamples);
        Sample cargaSample;
        ArrayList<Sample> exemplos = new ArrayList();
        while ((cargaSample = samples.next()) != null) {
            exemplos.add(cargaSample);
        }
        samples.close();
        MLP mlp = MLP.deserialize(propMLP.getFileMLP());

        exemplos.forEach(sample -> {
            mlp.updateIn(sample);
            OutPerceptron[] ret = mlp.process();
            StringBuilder sb = new StringBuilder(" ");
            for (OutPerceptron value : ret) {
                sb.append(String.format("%.30f ", value.getOut()));
            }
            System.out.printf("Exemplo in[%s] out esperado [%s] out real [%s]\n", sample.toStringIn(), sample.toStringOut(), sb.toString().trim());
        });
    }
}

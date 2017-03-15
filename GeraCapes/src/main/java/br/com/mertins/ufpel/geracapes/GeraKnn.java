package br.com.mertins.ufpel.geracapes;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.preparacao.Attribute;
import br.com.mertins.ufpel.avaliacao.util.TrainerKNNProperty;
import br.com.mertins.ufpel.knn.Knn;
import br.com.mertins.ufpel.knn.KnnInformation;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class GeraKnn {

    public void run(Properties properties) throws IOException {
        TrainerKNNProperty propKnn = new TrainerKNNProperty();
        propKnn.setNormalize((String) properties.get("normalize"));
        propKnn.setFirstLineAttribute((String) properties.get("firstlineattribute"));
        propKnn.setColumnLabel((String) properties.get("columnlabel"));
        propKnn.setFileTrainer((String) properties.get("filetrainer"));
        propKnn.setFileAval((String) properties.get("fileAval"));
        propKnn.setFileResult((String) properties.get("fileresult"));
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

        try (CSVReader reader = new CSVReader(new FileReader(propKnn.getFileAval()), ',')) {
            String[] colunas;
//            if ((colunas = reader.readNext()) != null) {
//                this.attributesOrigin = new ArrayList<>();
//                int pos = 0;
//                for (String valor : colunas) {
//                    attributesOrigin.add(new Attribute(pos++, this.parameters.isFirstLineAttribute() ? valor : String.format("Attrib %d", pos)));
//                }
//            }
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
//            int saidaCorreta = Integer.valueOf(sampleAval.getOutOriginal(1));
        }

    }

}

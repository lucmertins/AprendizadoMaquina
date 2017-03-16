package br.com.mertins.ufpel.geracapes;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.util.TrainerKNNProperty;
import br.com.mertins.ufpel.knn.Knn;
import br.com.mertins.ufpel.knn.KnnInformation;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        propKnn.setFileAval((String) properties.get("fileaval"));
        propKnn.setFileResult((String) properties.get("fileresult"));
        propKnn.setFileOrigAval((String) properties.get("fileorigaval"));
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
        File fileSamples = new File(propKnn.getFileTrainer());
        samples.avaliaFirstLine(fileSamples);
        samples.open(fileSamples);
        Sample cargaSample;
        ArrayList<Sample> exemplos = new ArrayList();
        while ((cargaSample = samples.next()) != null) {
            exemplos.add(cargaSample);
        }
        samples.close();
        List<Integer> listaValores = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(propKnn.getFileAval()), ',')) {
            String[] colunas;

            while ((colunas = reader.readNext()) != null) {
                List<KnnInformation> lista = new ArrayList<>();
                int pos = 0;
                List<Double> attributes = new ArrayList<>();

                for (String valor : colunas) {
                    if (pos != 1 && pos != 8) {   // não avaliando colunas 1 e 8
                        attributes.add(Double.valueOf(valor));
                    }
                    pos++;
                }
                Knn knnAval = new Knn(attributes.size());
                knnAval.setAttributes(attributes);
                int linha = 1;
                for (Sample exemploAval : exemplos) {
                    Knn knnBase = Knn.instanceLabel(exemploAval);
                    double distance = knnAval.distance(knnBase);
                    lista.add(new KnnInformation(linha++, distance, exemploAval.getOutOriginal(1)));
                }
                Collections.sort(lista);
                int saidaEscolhida = Knn.dominante(propKnn.parseValueK(), lista);
                listaValores.add(saidaEscolhida);
            }
        }

        Integer[] valores = listaValores.toArray(new Integer[0]);
        int pos = 0;
        boolean first = true;
        List<String> resultados = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(propKnn.getFileOrigAval()))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                StringBuilder resultado = new StringBuilder(linha);
                if (first) {
                    first = false;
                } else {
                    resultado.append(",");
                    resultado.append(PreparaArquivo.reverteRotulo(valores[pos++]));
                }
                resultados.add(resultado.toString());
            }
        }
        try (BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(propKnn.getFileResult()))) {
            for (String linha : resultados) {
                bufferWrite.write(linha);
                bufferWrite.write("\n");
            }
        }
    }

}

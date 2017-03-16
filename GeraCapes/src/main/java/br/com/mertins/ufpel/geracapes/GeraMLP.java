package br.com.mertins.ufpel.geracapes;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.OutPerceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import br.com.mertins.ufpel.avaliacao.util.TrainerMLPProperty;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class GeraMLP {

    public void run(Properties properties) throws IOException, ClassNotFoundException {
        TrainerMLPProperty propMLP = new TrainerMLPProperty();

        propMLP.setFileAval((String) properties.get("fileaval"));
        propMLP.setFileResult((String) properties.get("fileresult"));
        propMLP.setFileOrigAval((String) properties.get("fileorigaval"));
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
        File fileAval = new File(propMLP.getFileAval());
        samples.avaliaFirstLine(fileAval);
        samples.open(fileAval);
        Sample avalSample;
        ArrayList<Sample> avalList = new ArrayList();
        while ((avalSample = samples.next()) != null) {
            avalList.add(avalSample);
        }
        samples.close();
        MLP mlp = MLP.deserialize(propMLP.getFileMLP());

        List<Integer> listaValores = new ArrayList<>();
        avalList.forEach(sample -> {
            mlp.updateIn(sample);
            OutPerceptron[] ret = mlp.process();
            StringBuilder sb = new StringBuilder(" ");
            double[] results = new double[ret.length];
            for (int i = 0; i < ret.length; i++) {
                results[i] = ret[i].getOut();
                sb.append(String.format("%.30f ", results[i]));
            }
            int valorDomin = valorDominante(results);
            listaValores.add(valorDomin);
//            System.out.printf("Exemplo in[%s] out real [%s] [%d][%s]\n", sample.toStringIn(),
//                    sb.toString().trim(), valorDomin, PreparaArquivo.reverteRotulo(results));
        });

        Integer[] valores = listaValores.toArray(new Integer[0]);
        int pos = 0;
        boolean first = true;
        List<String> resultados = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(propMLP.getFileOrigAval()))) {
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

        try (BufferedWriter bufferWrite = new BufferedWriter(new FileWriter(propMLP.getFileResult()))) {
            for (String linha : resultados) {
                bufferWrite.write(linha);
                bufferWrite.write("\n");
            }
        }
    }

    public int valorDominante(double[] values) {
        int pos = -1;
        double max = -1;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                pos = i;
                max = values[i];
            }
        }
        return pos;
    }
}

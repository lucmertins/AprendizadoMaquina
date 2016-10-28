package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mertins
 */
public class ExecTreinamento {

    private SamplesParameters samplesParameters;
    private File fileTraining;
    private File fileTest;
    private File folder;
    private List<FileWriter> outList;

    public int open(SamplesParameters samplesParameters, File fileTraining, File fileTest) throws IOException {
        this.samplesParameters = samplesParameters;
        this.fileTraining = fileTraining;
        this.fileTest = fileTest;

        Samples samples = new Samples(samplesParameters);
        samples.avaliaFirstLine(fileTraining);
        samples.notRemoveAttributes();
        this.preparaArmazenamento();
        return samples.amountAttibutes();
    }

    public void run(boolean blocbkIfBadErr, final double rateTraining, final double moment, int epocas, MLP rede) throws IOException {

    }

    private void preparaArmazenamento() throws IOException {
        String property = System.getProperty("user.home");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date data = new Date();
        folder = new File(String.format("%s%sIARedeNeural%s%s", property, File.separator, File.separator, sdf.format(data)));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.outList = new ArrayList<>();
        String nome = String.format("%s%streinamento.txt", folder.getAbsolutePath(), File.separator);
        FileWriter out = new FileWriter(nome);
        this.outList.add(out);
        out.write(String.format("%s\n", sdf.format(data)));
        out.write(String.format("Normalizado: %b\n", samplesParameters.isNormalize()));
        out.write(String.format("Arquivo de treino %s\n", fileTraining.getAbsolutePath()));
        out.flush();
    }

}

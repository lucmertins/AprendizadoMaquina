package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<String> labelList;
    private List<FileWriter> outList;

    public void open(SamplesParameters samplesParameters, File fileTraining, File fileTest, String label) throws IOException {
        this.open(samplesParameters, fileTraining, fileTest, new ArrayList<>(Arrays.asList(new String[]{label})));
    }

    public void open(SamplesParameters samplesParameters, File fileTraining, File fileTest, String[] label) throws IOException {
        this.open(samplesParameters, fileTraining, fileTest, new ArrayList<>(Arrays.asList(label)));
    }

    public void open(SamplesParameters samplesParameters, File fileTraining, File fileTest, List<String> labelList) throws IOException {
        this.samplesParameters = samplesParameters;
        this.fileTraining = fileTraining;
        this.fileTest = fileTest;
        this.labelList = labelList;
        this.preparaArmazenamento();
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
        for (String label : this.labelList) {
            String nome = String.format("%s%streinamento_%s.txt", folder.getAbsolutePath(), File.separator, label);
            FileWriter out = new FileWriter(nome);
            this.outList.add(out);
            out.write(String.format("%s\n", sdf.format(data)));
            out.write(String.format("Label: %s\n", label));
            out.write(String.format("Normalizado: %b\n", samplesParameters.isNormalize()));
            out.write(String.format("Arquivo de treino %s\n", fileTraining.getAbsolutePath()));
            out.flush();
        }
    }

}

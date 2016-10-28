package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import br.com.mertins.ufpel.am.redeneural.Training;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecTreinamento {

    private SamplesParameters samplesParameters;
    private File fileTraining;
    private File fileTest;
    private File folder;
    private FileWriter outLog;

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
        FileWriter out = ExecTreinamento.this.outLog;
        Instant inicioTreinamento = Instant.now();
        Samples samples = new Samples(samplesParameters);
        samples.avaliaFirstLine(fileTraining);
        samples.notRemoveAttributes();

        out.write(String.format("Camada entrada: %d\n", rede.amountIn()));
        for (int i = 1; i <= rede.amountHiddenLayer(); i++) {
            out.write(String.format("Camada oculta %d -> %d perceptrons\n", i, rede.amountPerceptronsHiddenLayer(i)));
        }
        out.write(String.format("Camada saida: %d\n", rede.amountOut()));
        out.write(String.format("Epocas: %d\n", epocas));
        out.write(String.format("Taxa de treinamento inicial: %.60f\n", rateTraining));
        out.write(String.format("Taxa de Moment: %.30f\n", moment));
        out.write(String.format("Encerra treinamento se módulo do erro aumentar: %b\n\n", blocbkIfBadErr));
        out.flush();
        samples.open(fileTraining);
        Training treino = new Training(blocbkIfBadErr);
        treino.addListenerObservatorTraining(new Observator(out));
        treino.withBackPropagation(rede, samples, rateTraining, moment, epocas, out);
        Duration duration = Duration.between(inicioTreinamento, Instant.now());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
        out.write(String.format("Tempo de treinamento [%s]\n", format));
        out.flush();
        out.close();
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
        String nome = String.format("%s%streinamento.txt", folder.getAbsolutePath(), File.separator);
        this.outLog = new FileWriter(nome);
        this.outLog.write(String.format("%s\n", sdf.format(data)));
        this.outLog.write(String.format("Normalizado: %b\n", samplesParameters.isNormalize()));
        this.outLog.write(String.format("Arquivo de treino %s\n", fileTraining.getAbsolutePath()));
        this.outLog.flush();
    }

    private class Observator implements ObservatorTraining {

        private final FileWriter out;

        public Observator(FileWriter out) {
            this.out = out;
        }

        @Override
        public void register(Duration duration, int epoca, double errEpoca) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
                out.write(String.format("Epoca [%d] errEpoca [%.30f]  %s\n", epoca, errEpoca, format));
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}

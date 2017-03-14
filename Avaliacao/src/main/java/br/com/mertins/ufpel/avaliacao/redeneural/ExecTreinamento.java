package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import br.com.mertins.ufpel.am.redeneural.PersistNet;
import br.com.mertins.ufpel.am.redeneural.Training;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        Samples samples = new Samples(samplesParameters, (String value, Sample sample) -> {
            for (int i = 0; i < 10; i++) {
                sample.addOut(Integer.valueOf(value) == i ? 1 : 0);
            }
        });
        samples.avaliaFirstLine(fileTraining);
        samples.notRemoveAttributes();
        this.preparaArmazenamento();
        return samples.amountAttibutes();
    }

    public void run(boolean blocbkIfBadErr, final double rateTraining, final double moment, int epocas, MLP rede) throws IOException {
        try (FileWriter out = this.outLog) {
            Instant inicioTreinamento = Instant.now();
            Samples samples = new Samples(samplesParameters, (String value, Sample sample) -> {
                for (int i = 0; i < rede.amountOut(); i++) {
                    sample.addOut(Integer.valueOf(value) == i ? 1 : 0);
                }
            });
            samples.avaliaFirstLine(fileTraining);
            samples.notRemoveAttributes();
            System.out.printf("Pasta de trabalho: %s\n", ExecTreinamento.this.folder.getAbsolutePath());
            out.write(String.format("Camada entrada: %d\n", rede.amountIn()));
            for (int i = 1; i <= rede.amountHiddenLayer(); i++) {
                out.write(String.format("Camada %d oculta: %d perceptrons - %s\n", i, rede.amountPerceptronsHiddenLayer(i), rede.algorithmLayerSimoid(i).toString()));
            }
            out.write(String.format("Camada saida: %d perceptrons - %s\n", rede.amountOut(), rede.algorithmOutSimoid()));
            out.write(String.format("Epocas: %d\n", epocas));
            out.write(String.format("Taxa de treinamento inicial: %.60f\n", rateTraining));
            out.write(String.format("Taxa de Moment: %.30f\n", moment));
            out.write(String.format("Encerra treinamento se módulo do erro aumentar: %b\n\n", blocbkIfBadErr));
            out.write(String.format("Frequencia salvar MLP: %d\n\n",this.samplesParameters.getSaveMLPFrequence()));
            out.flush();
            samples.open(fileTraining);
            Training treino = new Training(blocbkIfBadErr);
            treino.addListenerObservatorTraining(new Observator(out));
            treino.withBackPropagation(rede, samples, rateTraining, moment, epocas, out, new PersistMLP(this.samplesParameters.getSaveMLPFrequence()));
            Instant now = Instant.now();
            Duration duration = Duration.between(inicioTreinamento, now);
            long days = ChronoUnit.DAYS.between(inicioTreinamento, now);
            String tempo = LocalTime.MIDNIGHT.plus(duration).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            out.write(String.format("Tempo de treinamento [%d dias, %s]\n", days, tempo));
            out.flush();
        }
    }

    public File getFolder() {
        return folder;
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
        String nome = String.format("%s%sIA_treinamento.txt", folder.getAbsolutePath(), File.separator);
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
        public void register(Duration duration, int epoca, double[] errEpoca) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
                double total = 0;
                for (int i = 0; i < errEpoca.length; i++) {
                    out.write(String.format("Out [%d] Epoca [%d] Erro [%.30f]  %s\n", i, epoca, errEpoca[i], format));
                    total += errEpoca[i];
                }
                total /= errEpoca.length;
                out.write(String.format("Erro Geral [%.30f]  %s\n", total, format));
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private class PersistMLP implements PersistNet {

        private final int frequence;
        private long count;

        public PersistMLP(int frequence) {
            this.frequence=frequence;
            this.count=0;
        }

        @Override
        public void save(MLP net, String value) throws IOException {
            count++;
            if (count % frequence == 0) {
                String name = String.format("%s%sMLP_%s", ExecTreinamento.this.folder.getAbsolutePath(), File.separator, value);
                MLP.serialize(net, name);
            }
        }
    }
}

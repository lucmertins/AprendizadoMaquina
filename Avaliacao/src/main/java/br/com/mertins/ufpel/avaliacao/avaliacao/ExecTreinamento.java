package br.com.mertins.ufpel.avaliacao.avaliacao;

import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.Training;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecTreinamento {

    private Samples samples;
    private File fileTraining;
    private File folder;
    private String label;
    private FileWriter out;

    ObservatorTraining observator = new ObservatorTraining() {
        @Override
        public void register(Duration duration, int epoca, double errEpoca) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
                out.write(String.format("Epoca [%d] errEpoca [%f]  %s\n", epoca, errEpoca, format));
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public void open(Samples samples, File file, String label) throws IOException {
        this.samples = samples;
        this.fileTraining = file;
        this.label = label;
        this.preparaArmazenamento();
    }

    public void close() throws IOException {
        this.out.close();
    }

    public void run(double rateTraining, int epocas, int tentativas, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        Instant inicioTreinamento = Instant.now();
        samples.avaliaFirstLine(fileTraining);
        List<Integer> remove = new ArrayList<>();
        samples.removeAttributesPos(remove);
        samples.open(fileTraining);
        samples.setTruePositive(this.label);
        int tempTentativas = 1;
        Training training = new Training();
        training.addListenerObservatorTraining(observator);
        Perceptron perceptronZero = training.withDelta(samples, rateTraining, epocas, algorithm);
        Perceptron.serialize(perceptronZero, String.format("%s%sperceptron_%s_%d", this.folder.getAbsolutePath(), File.separator, this.label, tempTentativas));
        while (tempTentativas < tentativas) {
            tempTentativas++;
            samples.reset();
            rateTraining = rateTraining / 10;
            perceptronZero = training.withDelta(samples, rateTraining, epocas, perceptronZero);
            Perceptron.serialize(perceptronZero, String.format("%s%sperceptron_%s_%d", this.folder.getAbsolutePath(), File.separator, this.label, tempTentativas));
        }
        samples.close();
        Duration duration = Duration.between(inicioTreinamento, Instant.now());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
        out.write(String.format("Tempo de treinamento [%s]\n", format));
        out.flush();
    }

    private void preparaArmazenamento() throws IOException {
        String property = System.getProperty("user.home");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date data = new Date();
        folder = new File(String.format("%s%sIAPerceptron%s%s", property, File.separator, File.separator, sdf.format(data)));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String nome = String.format("%s%sLog.txt", folder.getAbsolutePath(), File.separator);
        this.out = new FileWriter(nome);
        out.write(String.format("%s\n", sdf.format(data)));
        out.write(String.format("Label: %s\n", this.label));
        out.write(String.format("Normalizado: %b\n", samples.isNormalize()));
        out.write(String.format("Arquivo de treino %s\n\n", fileTraining.getAbsolutePath()));
        out.flush();

    }
}

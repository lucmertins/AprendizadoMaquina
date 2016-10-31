package br.com.mertins.ufpel.avaliacao.perceptron;

import br.com.mertins.ufpel.am.perceptron.FunctionSampleOut;
import br.com.mertins.ufpel.am.perceptron.ObservatorTraining;
import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.Sample;
import br.com.mertins.ufpel.am.perceptron.Samples;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecTreinamento {

    public enum Treinamento {
        DELTA, ESTOCASTICO
    }
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

    public void run(boolean blocbkIfBadErr, final double rateTraining, final double moment, int epocas, int tentativas, Treinamento treinamento, Perceptron.AlgorithmSimoid algorithm) throws IOException {
        int elem = 0;
        for (String label : this.labelList) {
            Thread thread = new Thread(new Process(label, elem++, blocbkIfBadErr, rateTraining, moment, epocas, tentativas, treinamento, algorithm));
            thread.setDaemon(false);
            thread.start();
        }
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

    private class Process implements Runnable {

        private final String label;
        private double rateTraining;
        private final double moment;
        private final int epocas;
        private final int tentativas;
        private final Perceptron.AlgorithmSimoid algorithm;
        private final int posFileLabel;
        private final boolean blocbkIfBadErr;
        private final Treinamento treinamento;

        public Process(String label, int pos, boolean blocbkIfBadErr, double rateTraining, double moment, int epocas, int tentativas, Treinamento treinamento, Perceptron.AlgorithmSimoid algorithm) {
            this.label = label;
            this.rateTraining = rateTraining;
            this.moment = moment;
            this.epocas = epocas;
            this.tentativas = tentativas;
            this.algorithm = algorithm;
            this.posFileLabel = pos;
            this.blocbkIfBadErr = blocbkIfBadErr;
            this.treinamento = treinamento;
        }

        @Override
        public void run() {
            FileWriter out = ExecTreinamento.this.outList.get(posFileLabel);
            Instant inicioTreinamento = Instant.now();
            Samples samples = new Samples(samplesParameters, new FunctionSampleOut() {
                @Override
                public void prepare(String value, Sample sample) {
                    sample.addOut(Integer.parseInt(value) == Integer.parseInt(Process.this.label) ? 1 : 0);                     // rever o código pois esta inadequado para testar perceptrons individualmente
                }
            });
            try {
                out.write(String.format("Tentativas: %d\n", this.tentativas));
                out.write(String.format("Epocas: %d\n", this.epocas));
                out.write(String.format("Taxa de treinamento inicial: %.30f\n", this.rateTraining));
                out.write(String.format("Taxa de Moment: %.30f\n", this.moment));
                out.write(String.format("Tipo treinamento: %s\n", this.treinamento));
                out.write(String.format("Encerra treinamento se módulo do erro aumentar: %b\n\n", this.blocbkIfBadErr));
                out.flush();
                samples.avaliaFirstLine(fileTraining);
                samples.notRemoveAttributes();
                samples.open(fileTraining);
                int tempTentativas = 1;
                Training treino = new Training(blocbkIfBadErr);
                treino.addListenerObservatorTraining(new Observator(out));
                Perceptron perceptron = treinamento == Treinamento.DELTA ? treino.withDelta(samples, rateTraining, moment, epocas, algorithm, out) : treino.withStochastic(samples, rateTraining, moment, epocas, algorithm, out);
                String name = String.format("%s%sperceptron_%s_%d", ExecTreinamento.this.folder.getAbsolutePath(), File.separator, label, tempTentativas);
                out.write(String.format("Perceptron [%s] taxa de treinamento [%.30f]\n", name, rateTraining));
                Perceptron.serialize(perceptron, name);
                out.flush();
                while (tempTentativas < tentativas) {
                    tempTentativas++;
                    samples.reset();
                    rateTraining = rateTraining / 5;
                    perceptron = treinamento == Treinamento.DELTA ? treino.withDelta(samples, rateTraining, moment, epocas, perceptron, out) : treino.withStochastic(samples, rateTraining, moment, epocas, perceptron, out);
                    name = String.format("%s%sperceptron_%s_%d", ExecTreinamento.this.folder.getAbsolutePath(), File.separator, label, tempTentativas);
                    out.write(String.format("Perceptron [%s] taxa de treinamento [%.30f]\n", name, rateTraining));
                    Perceptron.serialize(perceptron, name);
                    out.flush();
                }
                samples.close();
                tempTentativas = 1;
                while (tempTentativas <= tentativas) {
                    name = String.format("%s%sperceptron_%s_%d", ExecTreinamento.this.folder.getAbsolutePath(), File.separator, label, tempTentativas);
                    out.write("\n");
                    for (Perceptron.AlgorithmSimoid algorit : Perceptron.AlgorithmSimoid.values()) {
                        out.write(String.format("Avaliando Perceptron [%s] [%s]\n", name, algorit));
                        ExecuteAvaliacao aval = new ExecuteAvaliacao(out, label);
                        aval.run(fileTest, samplesParameters, name, algorit);
                    }
                    tempTentativas++;

                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, "Falha na thread de treinamento", ex);
            } finally {
                try {
                    Duration duration = Duration.between(inicioTreinamento, Instant.now());
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                    String format = fmt.format(duration.addTo(LocalDateTime.of(0, 1, 1, 0, 0)));
                    out.write(String.format("Tempo de treinamento [%s]\n", format));
                    out.flush();
                    out.close();

                } catch (IOException ex) {
                    Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, "Falha ao finalizar thread de treinamento", ex);
                }
            }
        }

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
                out.write(String.format("Epoca [%d] errEpoca [%.30f]  %s\n", epoca, errEpoca[0], format));
                out.flush();
            } catch (IOException ex) {
                Logger.getLogger(ExecTreinamento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}

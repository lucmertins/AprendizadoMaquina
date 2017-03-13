package br.com.mertins.ufpel.avaliacao.redeneural;

import br.com.mertins.ufpel.am.perceptron.Perceptron;
import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.am.redeneural.MLP;
import br.com.mertins.ufpel.avaliacao.util.Layer;
import br.com.mertins.ufpel.avaliacao.util.MessageQueue;
import br.com.mertins.ufpel.avaliacao.util.StringAsNumberComparator;
import br.com.mertins.ufpel.avaliacao.util.TrainerMLPProperty;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class ExecuteMLP {

    private BlockingQueue<MessageQueue> queue;

    public void training(Properties properties) {
        try {
            TrainerMLPProperty propMPL = new TrainerMLPProperty();
            propMPL.setNormalize((String) properties.get("normalize"));
            propMPL.setFirstLineAttribute((String) properties.get("firstlineattribute"));
            propMPL.setColumnLabel((String) properties.get("columnlabel"));
            propMPL.setFileTrainer((String) properties.get("filetrainer"));
            propMPL.setFileTest((String) properties.get("filetest"));
            propMPL.setHiddenLayer((String) properties.get("hiddenlayer"));
            propMPL.setOutputLayer((String) properties.get("outputlayer"));
            propMPL.setRateTraining((String) properties.get("ratetraining"));
            propMPL.setMoment((String) properties.get("moment"));
            propMPL.setEpoch((String) properties.get("epoch"));
            propMPL.setBlockIfBadErr((String) properties.get("blockifbaderr"));
            propMPL.setRemoveColumns((String) properties.get("removecolumns"));
            SamplesParameters parameters = new SamplesParameters();
            parameters.setNormalize(propMPL.parseNormalize());
            parameters.setFirstLineAttribute(propMPL.parseFirstLineAttribute());
            parameters.setColumnLabel(propMPL.parseColumnLabel());
            parameters.setRemoveColumns(propMPL.parseRemoveColumn());
            File fileTreinamento = new File(propMPL.getFileTrainer());
            File fileTest = new File(propMPL.getFileTest());
            ExecTreinamento execTreino = new ExecTreinamento();
            int numParametros = execTreino.open(parameters, fileTreinamento, fileTest);
            MLP rede = new MLP();
            rede.createIn(numParametros);
            propMPL.parseHiddenLayer().forEach((layer) -> {
                rede.addHiddenLayer(layer.getSize(), Perceptron.algorithm(layer.getAlgoritm()));
            });
            Layer outputLayer = propMPL.parseOutputLayer();
            rede.addOut(outputLayer.getSize(), Perceptron.algorithm(outputLayer.getAlgoritm()));
            rede.connect();
            propMPL.parseHiddenLayer();

            queue = new ArrayBlockingQueue<>(propMPL.parseEpoch());

            // thread para consumir arquivos novos no diretório, um de cada vez
            Thread thread = new Thread(new AvaliaMLP(execTreino.getFolder(), propMPL));
            thread.setDaemon(false);
            thread.start();
            execTreino.run(propMPL.parseBlockIfBadErr(), propMPL.parseRateTraining(), propMPL.parseMoment(), propMPL.parseEpoch(), rede);
            Thread.sleep(5000);  //aguarda um interva-lo, para o filesystem detectar que tem o último arquivo para avaliar
            queue.add(new MessageQueue("", true));
        } catch (Exception ex) {
            Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    public void evaluation(Properties properties) {
        try {
            TrainerMLPProperty propMPL = new TrainerMLPProperty();
            propMPL.setNormalize((String) properties.get("normalize"));
            propMPL.setFirstLineAttribute((String) properties.get("firstlineattribute"));
            propMPL.setColumnLabel((String) properties.get("columnlabel"));
            propMPL.setFileTrainer((String) properties.get("filetrainer"));
            propMPL.setFileTest((String) properties.get("filetest"));
            propMPL.setHiddenLayer((String) properties.get("hiddenlayer"));
            propMPL.setOutputLayer((String) properties.get("outputlayer"));
            propMPL.setFolderMLPs((String) properties.get("folderMLPs"));

            String nome = String.format("%s%sIA_avaliacao.txt", propMPL.getFolderMLPs(), File.separator);
            try (FileWriter outLog = new FileWriter(nome)) {
                File folderMLPs = new File(propMPL.getFolderMLPs());
                List<File> mlps = Arrays.asList(folderMLPs.listFiles((File dir, String name) -> name.startsWith("MLP")));
                Collections.sort(mlps, new StringAsNumberComparator("MLP_", false));
                for (File file : mlps) {
                    this.evalOne(file, propMPL, outLog);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
        }
    }

    private void evalOne(File fileMPL, TrainerMLPProperty propMPL, FileWriter outLog) throws IOException, ClassNotFoundException {
        outLog.write(String.format("\n\n%s\n", fileMPL.getAbsoluteFile()));
        SamplesParameters parameters = new SamplesParameters();
        parameters.setNormalize(propMPL.parseNormalize());
        parameters.setFirstLineAttribute(propMPL.parseFirstLineAttribute());
        parameters.setColumnLabel(propMPL.parseColumnLabel());

        File fileTest = new File(propMPL.getFileTest());
        ExecuteAvaliacao aval = new ExecuteAvaliacao();
        Accumulator[] acumuladores1 = aval.run(fileTest, parameters, fileMPL.getAbsolutePath());
        ConfusionMatrix confusao = new ConfusionMatrix();
        confusao.resumo(acumuladores1, outLog);
        outLog.write(String.format("\n"));
        confusao.matrix(acumuladores1, outLog);
        outLog.write(String.format("\nGerais    Acurácia [%.12f]    Precisão [%.12f]    Recall [%.12f]    F1 [%.12f]\n",
                confusao.accuracy(acumuladores1), confusao.precision(acumuladores1), confusao.recall(acumuladores1), confusao.f1(acumuladores1)));
        outLog.flush();
    }

    private class MonitorCreateMLP implements Runnable {

        private final Path dir;

        public MonitorCreateMLP(Path dir) throws IOException {
            this.dir = dir;
        }

        @Override
        public void run() {
            try {
//                System.out.println("Rodando thread " + dir.getFileName());
                WatchService watchService = dir.getFileSystem().newWatchService();
                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                WatchKey watchKey = null;
                while (true) {
                    watchKey = watchService.poll();
                    if (watchKey != null) {
                        watchKey.pollEvents().stream().forEach(event -> ExecuteMLP.this.queue.add(new MessageQueue(event.context().toString(), false)));
                        watchKey.reset();
                    }
                    Thread.sleep(500);
                }
            } catch (IOException ex) {
                Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, "Não foi possível monitorar criação de redes", ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, "Não foi possível esperar 1s para monitorar a criação da rede", ex);
            }
        }
    }

    private class AvaliaMLP implements Runnable {

        private final FileWriter outLog;
        private final TrainerMLPProperty propMPL;
        private final File folder;

        public AvaliaMLP(File folder, TrainerMLPProperty propMPL) throws IOException {
            this.propMPL = propMPL;
            this.folder = folder;
            String nome = String.format("%s%sIA_avaliacao.txt", folder.getAbsolutePath(), File.separator);
            outLog = new FileWriter(nome);
        }

        @Override
        public void run() {
            try {
                // thread para monitorar arquivos novos no diretório
                Thread thread = new Thread(new MonitorCreateMLP(this.folder.toPath()));
                thread.setDaemon(true);
                thread.start();

                MessageQueue msg;
                while (!(msg = queue.take()).isFinished()) {
                    Thread.sleep(500);
                    File file = new File(String.format("%s%s%s", this.folder.getAbsolutePath(), File.separator, msg.getMsg()));
                    ExecuteMLP.this.evalOne(file, propMPL, outLog);
                }
                this.outLog.close();
            } catch (InterruptedException | IOException | ClassNotFoundException ex) {
                Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, "Falha ao avaliar em paralelo as MLPs", ex);
            }
        }

    }
}

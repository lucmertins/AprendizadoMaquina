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
public class ExecuteMLP implements Runnable {

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
            SamplesParameters parameters = new SamplesParameters();
            parameters.setNormalize(propMPL.parseNormalize());
            parameters.setFirstLineAttribute(propMPL.parseFirstLineAttribute());
            parameters.setColumnLabel(propMPL.parseColumnLabel());
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
            Thread thread = new Thread(new MonitorCreateMLP(execTreino.getFolder().toPath(), propMPL));
            thread.setDaemon(true);
            thread.start();
            thread = new Thread(this);
            thread.setDaemon(false);
            thread.start();
            execTreino.run(propMPL.parseBlockIfBadErr(), propMPL.parseRateTraining(), propMPL.parseMoment(), propMPL.parseEpoch(), rede);

            queue.add(new MessageQueue("", true));
        } catch (Exception ex) {
            Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecTreinamento.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
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
                FilenameFilter filter = (File dir, String name) -> name.startsWith("MLP");
                List<File> mlps = Arrays.asList(folderMLPs.listFiles(filter));
                Collections.sort(mlps, new StringAsNumberComparator());
                for (File file : mlps) {
                    this.evalOne(file, propMPL, outLog);
                    outLog.flush();
                }
                outLog.flush();
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(br.com.mertins.ufpel.avaliacao.perceptron.ExecuteAvaliacao.class.getName()).log(Level.SEVERE, String.format("Falha ao avaliar testes [%s]", ex.getMessage()), ex);
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
    }

    @Override
    public void run() {
        
//         String nome = String.format("%s%sIA_avaliacao.txt", propMPL.getFolderMLPs(), File.separator);
//                try (FileWriter outLog = new FileWriter(nome)) {
//                    File file = new File(String.format("%s%s%s", propMPL.getFolderMLPs(), File.separator));
//                    ExecuteMLP.this.evalOne(file, propMPL, outLog);
//                    outLog.flush();
//
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, null, ex);
//                }

        
        try {
            MessageQueue msg;
            //consuming messages until exit message is received
            while (!(msg = queue.take()).isFinished()) {
                Thread.sleep(10);
                System.out.println("Consumed " + msg.getMsg());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class MonitorCreateMLP implements Runnable {

        private final Path dir;
        private final TrainerMLPProperty propMPL;

        public MonitorCreateMLP(Path dir, TrainerMLPProperty propMPL) throws IOException {
            this.dir = dir;
            this.propMPL = propMPL;
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
}

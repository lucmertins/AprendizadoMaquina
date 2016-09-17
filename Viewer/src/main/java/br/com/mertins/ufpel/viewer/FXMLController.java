package br.com.mertins.ufpel.viewer;

import br.com.mertins.ufpel.am.id3.ID3;
import br.com.mertins.ufpel.am.id3.PostPruning;
import br.com.mertins.ufpel.am.id3.Rules;
import br.com.mertins.ufpel.am.preparacao.Sample;
import br.com.mertins.ufpel.am.tree.Node;
import br.com.mertins.ufpel.am.tree.NodeBase;
import br.com.mertins.ufpel.am.validate.Indicatives;
import br.com.mertins.ufpel.am.validate.Investigate;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController {

    @FXML
    private AnchorPane principal;
    @FXML
    private TextField txtFileChoose, txtRemoverAtributos;
    @FXML
    private Accordion acPrincipal;
    @FXML
    private TitledPane pnCarga, pnPreparacao, pnTeste, pnID3, pnLog;
    @FXML
    private TextArea txtResultado, txtAtributos, txtMsg;
    @FXML
    private ChoiceBox<Choice> cmbLabel;
    @FXML
    private RadioButton rbFirstLineCabecalho;
    private Sample sample;

    @FXML
    void initialize() {
        ConfigProperties conf = new ConfigProperties();
        String pathFileSource = conf.load("pathFileSource");
        txtFileChoose.setText(pathFileSource);
        acPrincipal.setExpandedPane(pnCarga);
    }

    @FXML
    protected void onClickChooseFile(MouseEvent event) {
        Stage stage = (Stage) principal.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o arquivo para avaliação");
        try {
            String text = txtFileChoose.getText();
            if (text != null && text.trim().length() > 0) {
                File file = new File(text);
                fileChooser.setInitialDirectory(file.getParentFile());
            }
        } catch (Exception ex) {
        }
        File selectedFile = null;
        try {
            selectedFile = fileChooser.showOpenDialog(stage);
        } catch (Exception ex) {
            fileChooser.setInitialDirectory(null);
            selectedFile = fileChooser.showOpenDialog(stage);
        }
        if (selectedFile != null) {
            txtFileChoose.setText(selectedFile.getAbsolutePath());
            ConfigProperties conf = new ConfigProperties();
            conf.save("pathFileSource", selectedFile.getAbsolutePath(), "");
        }
    }

    @FXML
    void mnclose(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    void onClickBtExec(ActionEvent event) {
        TitledPane paneSelect = acPrincipal.getExpandedPane();
        String opc = paneSelect == null ? "" : paneSelect.getId();

        switch (opc) {
            case "pnCarga":
                if (this.execCarga()) {
                    acPrincipal.setExpandedPane(pnPreparacao);
                } else {
                    acPrincipal.setExpandedPane(pnLog);
                }
                break;
            case "pnPreparacao":
                if (this.execPreparacao()) {
                    acPrincipal.setExpandedPane(pnTeste);
                } else {
                    acPrincipal.setExpandedPane(pnLog);
                }
                break;
            case "pnTeste":
                if (this.execTreinamento()) {
                    acPrincipal.setExpandedPane(pnID3);
                } else {
                    acPrincipal.setExpandedPane(pnLog);
                }
                break;
            case "pnID3":
                if (this.execID3()) {
                    acPrincipal.setExpandedPane(pnID3);
                } else {
                    acPrincipal.setExpandedPane(pnLog);
                }

                break;

        }

    }

    private boolean execCarga() {
        txtMsg.setText(null);
        try {
            String fileName = txtFileChoose.getText();
            sample = new Sample();
            sample.setFirstLineAttribute(rbFirstLineCabecalho.isSelected());
            sample.avaliaFirstLine(fileName);
            txtAtributos.setText(null);
            cmbLabel.getItems().clear();
            ObservableList<Choice> choices = FXCollections.observableArrayList();
            sample.getAttributesOrigin().forEach(attribute -> {
                txtAtributos.appendText(String.format("%d - %s\n", attribute.getPosition(), attribute.getName()));
                choices.add(new Choice(attribute.getPosition(), attribute.getName()));

            });
            cmbLabel.setItems(choices);
            if (!choices.isEmpty()) {
                cmbLabel.setValue(choices.get(choices.size() - 1));
            }
            return true;
        } catch (Exception e) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
            txtMsg.setText(String.format("Execução %s\n", sdf.format(new Date())));
            txtMsg.appendText(String.format("Erro na abertura do arquivo: %s.\n", e.getMessage()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            txtMsg.appendText(new String(baos.toByteArray(), StandardCharsets.UTF_8));
            return false;
        }
    }

    private boolean execPreparacao() {
        txtMsg.setText(null);
        try {
            String[] split = txtRemoverAtributos.getText().split(",");
            List<Integer> remove = new ArrayList<>();
            for (String value : split) {
                try {
                    remove.add(Integer.valueOf(value));
                } catch (Exception ex) {
                }
            }
            sample.removeAttributesPos(remove);
            sample.defineColumnLabel(cmbLabel.getValue().id);
            return true;
        } catch (Exception ex) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
            txtMsg.setText(String.format("Execução %s\n", sdf.format(new Date())));
            txtMsg.appendText(String.format("Falha ao preparar fase [%s] .\n", ex.getMessage()));
            return false;
        }
    }

    private boolean execTreinamento() {
        txtMsg.setText(null);
        txtResultado.setText(null);
        return true;
    }

    private boolean execID3() {
        txtMsg.setText(null);
        if (sample != null) {
            try {
                String fileName = txtFileChoose.getText();
                sample.process(fileName);
                ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes(), sample.getLabels());
                Node root = id3.process();
                StringBuilder print = root.print();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                txtResultado.setText(String.format("Execução %s\n", sdf.format(new Date())));
                txtResultado.appendText(print.toString());
                Investigate investigate = new Investigate(root, sample.getRegisters(), sample.getLabels());
                Indicatives indicativo = investigate.process();
                sample.getLabels().forEach(label -> {
                    String format = String.format("\n[%s]\t\t", label.getValue());
                    txtResultado.appendText(format);
                    format = String.format("VP %d   FP %d   VN %d   FN %d\n", indicativo.getTruePositives(label).intValue(),
                            indicativo.getFalsePositives(label).intValue(),
                            indicativo.getTrueNegatives(label).intValue(), indicativo.getFalseNegatives(label).intValue());
                    txtResultado.appendText(format);
                    format = String.format("\t\tPrecisão %f    Recall %f    F1 %f\n", indicativo.precision(label).doubleValue(),
                            indicativo.recall(label).doubleValue(), indicativo.f1(label).doubleValue());
                    txtResultado.appendText(format);

                });
                txtResultado.appendText(String.format("\nAcurácia %f\n", indicativo.accuracy().doubleValue()));
//                txtResultado.appendText("\nMatriz de Confusão\n");
//                Label lbPositive = Label.positive(sample.getLabels());
//                Label lbNegative = Label.negative(sample.getLabels());
//                format = String.format("\t\t\t%s\t\t%s\n", lbPositive != null ? lbPositive.getValue() : "?", lbNegative != null ? lbNegative.getValue() : "?");
//                txtResultado.appendText(format);
//                format = String.format("\t%s\t\t%d\t\t%d\n", lbPositive != null ? lbPositive.getValue() : "?", indicativos.getVerdadeirosPositivos().intValue(), indicativos.getFalsosPositivos().intValue());
//                txtResultado.appendText(format);
//                format = String.format("\t%s\t\t%d\t\t%d\n", lbNegative != null ? lbNegative.getValue() : "?", indicativos.getFalsosNegativos().intValue(), indicativos.getVerdadeirosNegativos().intValue());
//                txtResultado.appendText(format);
                txtResultado.appendText("\nRegras\n");
                Rules rules = Rules.instance(root);
                print = rules.print();
                txtResultado.appendText(print.toString());
                txtResultado.appendText("\nPoda\n");
                PostPruning pruning = new PostPruning(root);
                NodeBase bestTree = pruning.process(sample.getRegisters(), sample.getLabels());
                txtResultado.appendText(bestTree.print().toString());
                Investigate investigateBest = new Investigate(bestTree, sample.getRegisters(), sample.getLabels());
                Indicatives indicativoBest = investigateBest.process();
                sample.getLabels().forEach(label -> {
                    txtResultado.appendText(String.format("Label [%s]\n", label.getValue()));
                    txtResultado.appendText(String.format("  VP %d   FP %d   VN %d   FN %d\n", indicativoBest.getTruePositives(label).intValue(), indicativoBest.getFalsePositives(label).intValue(),
                            indicativoBest.getTrueNegatives(label).intValue(), indicativoBest.getFalseNegatives(label).intValue()));
                    txtResultado.appendText(String.format("  Precisão %f    Recall %f    F1 %f\n", indicativoBest.precision(label).doubleValue(), indicativoBest.recall(label).doubleValue(), indicativoBest.f1(label).doubleValue()));
                });
                txtResultado.appendText(String.format("\nAcurácia %f\n", indicativoBest.accuracy().doubleValue()));

                return true;
            } catch (Exception e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                txtMsg.setText(String.format("Execução %s\n", sdf.format(new Date())));
                txtMsg.appendText(String.format("Erro: %s.\n", e.getMessage()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                e.printStackTrace(ps);
                txtMsg.appendText(new String(baos.toByteArray(), StandardCharsets.UTF_8));
                return false;
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
            txtMsg.setText(String.format("Execução %s\n", sdf.format(new Date())));
            txtMsg.appendText(String.format("Fase de carga precisa ser executada.\n"));
            return false;
        }

    }

    class Choice {

        Integer id;
        String displayString;

        Choice(Integer id) {
            this(id, null);
        }

        Choice(String displayString) {
            this(null, displayString);
        }

        Choice(Integer id, String displayString) {
            this.id = id;
            this.displayString = displayString;
        }

        @Override
        public String toString() {
            return displayString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Choice choice = (Choice) o;
            return displayString != null && displayString.equals(choice.displayString) || id != null && id.equals(choice.id);
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (displayString != null ? displayString.hashCode() : 0);
            return result;
        }
    }
}

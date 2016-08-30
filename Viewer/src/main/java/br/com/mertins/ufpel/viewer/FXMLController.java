package br.com.mertins.ufpel.viewer;

import br.com.mertins.ufpel.am.id3.ID3;
import br.com.mertins.ufpel.am.preparacao.Sample;
import br.com.mertins.ufpel.am.tree.Node;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
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
    private TextField txtFileChoose, txtRemoverAtributos, txtLabelPositivo;
    @FXML
    private Accordion acPrincipal;
    @FXML
    private TitledPane pnPreparacao;
    @FXML
    private TitledPane pnID3;
    @FXML
    private TextArea txtResultado, txtAtributos;
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
        acPrincipal.setExpandedPane(pnPreparacao);
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
            case "pnPreparacao":
                this.execPreparacao();
                break;
            case "pnTreinamento":
                this.execTreinamento();
                acPrincipal.setExpandedPane(pnID3);
                break;
            case "pnID3":
                this.execID3();
                break;

        }

    }

    private void execPreparacao() {
        try {
            String fileName = txtFileChoose.getText();
            sample = new Sample();
            sample.setFirstLineAttribute(rbFirstLineCabecalho.isSelected());
            try (FileReader arq = new FileReader(fileName)) {
                BufferedReader lerArq = new BufferedReader(arq);
                sample.avaliaFirstLine(lerArq);
            }
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
        } catch (Exception e) {
            txtResultado.setText(String.format("Erro na abertura do arquivo: %s.\n", e.getMessage()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            txtResultado.appendText(new String(baos.toByteArray(), StandardCharsets.UTF_8));
        }
    }

    private void execTreinamento() {

    }

    private void execID3() {
        if (sample != null) {
            try {
                String fileName = txtFileChoose.getText();
                String[] split = txtRemoverAtributos.getText().split(",");
                List<Integer> remove = new ArrayList<>();
                for (String value : split) {
                    try {
                        remove.add(Integer.valueOf(value));
                    } catch (Exception ex) {
                    }
                }
                sample.removeAttributesPos(remove);
                sample.defineColumnLabel(cmbLabel.getValue().id, txtLabelPositivo.getText());
                try (FileReader arq = new FileReader(fileName)) {
                    BufferedReader lerArq = new BufferedReader(arq);
                    sample.process(lerArq);
                }
                ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes());
                Node root = id3.process();
                StringBuilder print = root.print();
                txtResultado.setText(print.toString());
            } catch (Exception e) {
                txtResultado.setText(String.format("Erro na abertura do arquivo: %s.\n", e.getMessage()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                e.printStackTrace(ps);
                txtResultado.appendText(new String(baos.toByteArray(), StandardCharsets.UTF_8));
            }
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

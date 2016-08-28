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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
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
    private TextField txtFileChoose;
    @FXML
    private Accordion acPrincipal;
    @FXML
    private TitledPane pnPreparacao;
    @FXML
    private TitledPane pnID3;
    @FXML
    private TextArea txtResultado;

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
            case "pnID3":
                this.execPreparacao();
                acPrincipal.setExpandedPane(pnID3);
                break;
        }

    }

    private void execPreparacao() {
        try {
            String fileName = txtFileChoose.getText();
            File file = new File(fileName);
            Sample sample = new Sample(5);
            sample.addDiscardedColumns(0);
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

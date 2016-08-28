package br.com.mertins.ufpel.viewer;

import br.com.mertins.ufpel.am.id3.ID3;
import br.com.mertins.ufpel.am.preparacao.Sample;
import br.com.mertins.ufpel.am.tree.Node;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
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
        this.exec();
    }

    private void exec() {
        try {
            String fileName = txtFileChoose.getText();

            File file = new File(fileName);
            System.out.printf("Arquivo %s\n", file.getAbsolutePath());
            Sample sample = new Sample(5);
            sample.addDiscardedColumns(0);
            try (FileReader arq = new FileReader(fileName)) {
                BufferedReader lerArq = new BufferedReader(arq);
                sample.process(lerArq);
            }
            System.out.println("******");
            ID3 id3 = new ID3(sample.getRegisters(), sample.getAttributes());
            Node root = id3.process();
            root.print();
            System.out.println("******");
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
    }
}

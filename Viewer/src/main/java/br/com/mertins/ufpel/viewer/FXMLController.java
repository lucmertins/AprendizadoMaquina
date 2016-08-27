package br.com.mertins.ufpel.viewer;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtFileChoose.setText(selectedFile.getAbsolutePath());
            ConfigProperties conf = new ConfigProperties();
            conf.save("pathFileSource", selectedFile.getAbsolutePath(), "");
        }
    }

    @FXML
    void initialize() {
        ConfigProperties conf = new ConfigProperties();
        String pathFileSource = conf.load("pathFileSource");
        txtFileChoose.setText(pathFileSource);

    }
}

package br.com.mertins.ufpel.geracapes;

import br.com.mertins.ufpel.am.perceptron.SamplesParameters;
import br.com.mertins.ufpel.avaliacao.redeneural.ExecuteMLP;
import br.com.mertins.ufpel.avaliacao.util.TrainerMLPProperty;
import com.opencsv.CSVReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mertins
 */
public class PreparaArquivo {

    public void exec(Properties properties) {
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
            propMPL.setFileResultColunaExtra((String) properties.get("fileresultcolunaextra"));
            SamplesParameters parameters = new SamplesParameters();
            parameters.setNormalize(propMPL.parseNormalize());
            parameters.setFirstLineAttribute(propMPL.parseFirstLineAttribute());
            parameters.setColumnLabel(propMPL.parseColumnLabel());
            parameters.setRemoveColumns(propMPL.parseRemoveColumn());

            String property = System.getProperty("user.home");
            File folderTemp = new File(String.format("%s%stemp", property, File.separator));
            if (!folderTemp.exists()) {
                folderTemp.mkdir();
            } else if (!folderTemp.isDirectory()) {
                System.out.println("Pasta temp não foi criada");
                System.exit(1);
            }
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.format("%s%stemp%sCapsTemp.cvs", property, File.separator, File.separator)), "utf-8"))) {
                System.out.printf("Arquivo reajustado: %s\n", String.format("%s%stemp%sCapesTemp.cvs", property, File.separator, File.separator));
                try (CSVReader reader = new CSVReader(new FileReader(propMPL.getFileTrainer()), ',')) {
                    String[] colunas;
                    boolean firstLine = true;
                    while ((colunas = reader.readNext()) != null) {
                        if (firstLine) {
                            firstLine = false;
                        } else if (colunas != null) {
                            int pos = propMPL.parseFileResultColunaExtra() ? -1 : 0;
                            StringBuilder linha = new StringBuilder();
                            for (String coluna : colunas) {
                                switch (pos) {
                                    case -1:
                                        break;
                                    case 0:
                                        linha.append(String.format("%s,", convertAreas(coluna)));
                                        break;
                                    case 2:
                                        linha.append(String.format("%s,", convertModalidade(coluna)));
                                        break;
                                    case 3:
                                        linha.append(String.format("%s,", convertInicioDr(coluna)));
                                        break;
                                    case 4:
                                        linha.append(String.format("%s,", convertInicioMs(coluna)));
                                        break;
                                    case 26:
                                        linha.append(String.format("%s,", convertRotulo(coluna)));
                                        break;
                                    default:
                                        linha.append(String.format("%s,", coluna));
                                        break;
                                }
                                pos++;
                            }
                            linha.deleteCharAt(linha.length() - 1);
                            writer.write(linha.toString());
                            writer.write("\n");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ExecuteMLP.class.getName()).log(Level.SEVERE, String.format("Falha ao treinar [%s]", ex.getMessage()), ex);
        }
    }

    private Map<String, Integer> areas = new HashMap<>();
    private Map<String, Integer> modalidade = new HashMap<>();

    public PreparaArquivo() {

    }

    private int convertAreas(String coluna) {
        int last = areas.size();
        Integer label = areas.get(coluna.trim());
        if (label == null) {
            areas.put(coluna.trim(), ++last);
            return last;
        } else {
            return label;
        }
    }

    private int convertModalidade(String coluna) {
        int last = modalidade.size();
        Integer label = modalidade.get(coluna.trim());
        if (label == null) {
            modalidade.put(coluna.trim(), ++last);
            return last;
        } else {
            return label;
        }
    }

    private int convertInicioDr(String coluna) {
        if (coluna.trim().length() == 0) {
            return 1900;
        } else {
            return Integer.valueOf(coluna.substring(0, coluna.indexOf('.')));
        }
    }

    private int convertInicioMs(String coluna) {
        if (coluna.trim().length() == 0) {
            return 1900;
        } else {
            return Integer.valueOf(coluna.substring(0, coluna.indexOf('.')));
        }
    }

    private int convertRotulo(String coluna) {
        switch (coluna.trim().toLowerCase()) {
            case "tres":
                return 0;
            case "quatro":
                return 1;
            case "cinco":
                return 2;
            case "seis":
                return 3;
            case "sete":
                return 4;
            default:
                return -1;
        }
    }

    public static String reverteRotulo(int value) {
        String[] desc = {"Tres", "Quatro", "Cinco", "Seis", "Sete"};
        return desc[value];
    }
}

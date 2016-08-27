package br.com.mertins.ufpel.viewer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

/**
 *
 * @author mertins
 */
public class ConfigProperties {

    public String load(String parameter) {
        File configFile = new File("AprendizadoMaquina.properties");
        FileReader reader = null;
        try {
            reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            return props.getProperty(parameter);

        } catch (Exception ex) {
        } finally {
            try {
                reader.close();
            } catch (Exception ex2) {

            }
        }
        return null;
    }

    public void save(String parameter, String value, String comments) {
        File configFile = new File("AprendizadoMaquina.properties");
        FileWriter writer = null;
        try {
            Properties props = new Properties();
            props.setProperty(parameter, value);
            writer = new FileWriter(configFile);
            props.store(writer, comments);
            writer.close();
        } catch (Exception ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex2) {

            }
        }
    }
}

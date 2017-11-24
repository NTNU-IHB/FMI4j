package no.sfi.mechatronics.fmu2jar;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;

public class Fmu2JarGUI {

    public Fmu2JarGUI() {

        JFileChooser fc1 = new JFileChooser();
        fc1.setDialogTitle("Select FMU");
        fc1.setFileFilter(new FileNameExtensionFilter("FMU FILES", "fmu"));

        String defaultDir = System.getenv("FMU_LIB");
        if (defaultDir != null) {
            fc1.setCurrentDirectory(new File(defaultDir));
        }

        if (fc1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            
            File fmuFile = fc1.getSelectedFile();

            JFileChooser fc2 = new JFileChooser();
            fc2.setDialogTitle("Save generated JAR");
            fc2.setFileFilter(new FileNameExtensionFilter("JAR FILES", "jar"));
            fc2.setCurrentDirectory(fc1.getSelectedFile().getParentFile());
            fc2.setSelectedFile(new File(FilenameUtils.getBaseName(fmuFile.getName()) + ".jar"));
            
            if (fc2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                Fmu2Jar.convert(fmuFile.getAbsolutePath(), fc2.getSelectedFile().getAbsolutePath());
            }
        }

        System.exit(0);

    }

    public static void main(String[] args) {
        new Fmu2JarGUI();
    }
}

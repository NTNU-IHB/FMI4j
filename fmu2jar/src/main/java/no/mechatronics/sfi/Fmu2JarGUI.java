/*
 * The MIT License
 *
 * Copyright 2017. Norwegian University of Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.mechatronics.sfi;

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

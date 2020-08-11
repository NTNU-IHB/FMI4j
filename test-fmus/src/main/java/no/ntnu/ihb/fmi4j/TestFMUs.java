package no.ntnu.ihb.fmi4j;

import java.io.File;

public class TestFMUs {

    public static File get(String path) {
        File projectFolder = new File(".").getAbsoluteFile();
        while (!projectFolder.getName().toLowerCase().equals("fmi4j")) {
            projectFolder = projectFolder.getParentFile();
        }
        return new File(projectFolder, "test-fmus/fmus" + File.separator + path).getAbsoluteFile();
    }

}

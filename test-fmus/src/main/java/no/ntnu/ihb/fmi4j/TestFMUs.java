package no.ntnu.ihb.fmi4j;

import java.io.File;

public class TestFMUs {

    public static File get(String path) {
        File projectFolder = new File(".").getAbsoluteFile();
        while (!projectFolder.getName().toLowerCase().equals("fmi4j")) {
            projectFolder = projectFolder.getParentFile();
        }
        String child = "test-fmus" + File.separator + "fmus" + File.separator + path;
        return new File(projectFolder, child).getAbsoluteFile();
    }

}

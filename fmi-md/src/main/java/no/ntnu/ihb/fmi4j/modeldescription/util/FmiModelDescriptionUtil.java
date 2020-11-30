package no.ntnu.ihb.fmi4j.modeldescription.util;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FmiModelDescriptionUtil {

    private static final String MODEL_DESC_FILE = "modelDescription.xml";

    public static String extractModelDescriptionXml(File file) throws IOException {
        return extractModelDescriptionXml(new FileInputStream(file));
    }

    public static String extractModelDescriptionXml(URL url) throws IOException {
        return extractModelDescriptionXml(url.openStream());
    }

    public static String extractModelDescriptionXml(InputStream is) throws IOException {

        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is))) {
            ZipEntry nextEntry;
            while ((nextEntry = zis.getNextEntry()) != null) {
                if (nextEntry.getName().equals(MODEL_DESC_FILE)) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(zis))) {
                        String line;
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        return sb.toString();
                    }
                }
            }
        }
        throw new IllegalArgumentException("Input is not an valid FMU! No " + MODEL_DESC_FILE + " present!");
    }

    public static String extractVersion(URL url) throws IOException {
        return extractVersion(extractModelDescriptionXml(url));
    }

    public static String extractVersion(File file) throws IOException {
        return extractVersion(extractModelDescriptionXml(file));
    }

    public static String extractVersion(String xml) {
        return JAXB.unmarshal(new StringReader(xml), MockupModelDescription.class).fmiVersion;
    }

    public static String extractGuid(URL url) throws IOException {
        return extractGuid(extractModelDescriptionXml(url));
    }

    public static String extractGuid(File file) throws IOException {
        return extractGuid(extractModelDescriptionXml(file));
    }

    public static String extractGuid(String xml) {
        return JAXB.unmarshal(new StringReader(xml), MockupModelDescription.class).guid;
    }

    @XmlRootElement(name = "fmiModelDescription")
    private static class MockupModelDescription {

        @XmlAttribute(name = "guid")
        public String guid;
        @XmlAttribute(name = "fmiVersion")
        public String fmiVersion;

    }

}

package no.ntnu.ihb.fmi4j.importer;

import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider;
import no.ntnu.ihb.fmi4j.modeldescription.parser.ModelDescriptionParser;

import java.io.File;
import java.io.IOException;

public class TestFMUs {

    private static String getPath() {
        return TestFMUs.class.getClassLoader().getResource("fmus").getFile();
    }


    public static FmiVersion20 fmi20() {

        return new FmiVersion20(new StringBuilder(TestFMUs.getPath()));
    }


    public static class FmiVersion20 {

        private final StringBuilder sb;

        public FmiVersion20(StringBuilder sb) {
            this.sb = sb.append("/2.0");
        }

        public CsType cs() {
            return new CsType(sb);
        }

        public MeType me() {
            return new MeType(sb);
        }

    }

    public static class CsType {

        private final StringBuilder sb;

        public CsType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/cs/");
            return new FmuVendor(sb, vendor);
        }

    }

    public static class MeType {

        private final StringBuilder sb;

        public MeType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/me/");
            return new FmuVendor(sb, vendor);
        }

    }

    public static class FmuVendor {

        private final StringBuilder sb;

        public FmuVendor(StringBuilder sb, String vendor) {
            this.sb = sb.append("/").append(vendor);
        }

        public FmuVersion version(String version) {
            return new FmuVersion(sb, version);
        }

    }

    public static class FmuVersion {

        private final StringBuilder sb;

        public FmuVersion(StringBuilder sb, String version) {
            this.sb = sb.append("/").append(version);
        }

        public FmuProvider name(String name) {
            sb.append("/").append(name).append("/").append(name).append(".fmu");
            return new FmuProvider(new File(sb.toString()));
        }
    }

    public static class FmuProvider {

        private final File fmuFile;

        public FmuProvider(File fmuFile) {
            this.fmuFile = fmuFile;
        }

        public Fmu fmu() throws IOException { return Fmu.from(fmuFile); }

        public ModelDescriptionProvider modelDescription() {
            return ModelDescriptionParser.parse(fmuFile);
        }

        public String modelDescriptionXml() {
            return ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        }

        public File file() {
            return fmuFile;
        }

    }

}
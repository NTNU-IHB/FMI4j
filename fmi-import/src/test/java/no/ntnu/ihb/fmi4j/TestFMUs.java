package no.ntnu.ihb.fmi4j;

import no.ntnu.ihb.fmi4j.importer.Fmu;
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

        FmiVersion20(StringBuilder sb) {
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

        CsType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/cs/");
            return new FmuVendor(sb, vendor);
        }

    }

    public static class MeType {

        private final StringBuilder sb;

        MeType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/me/");
            return new FmuVendor(sb, vendor);
        }

    }

    public static class FmuVendor {

        private final StringBuilder sb;

        FmuVendor(StringBuilder sb, String vendor) {
            this.sb = sb.append("/").append(vendor);
        }

        public FmuVersion version(String version) {
            return new FmuVersion(sb, version);
        }

    }

    public static class FmuVersion {

        private final StringBuilder sb;

        FmuVersion(StringBuilder sb, String version) {
            this.sb = sb.append("/").append(version);
        }

        public Fmu fmu(String name) throws IOException {
            sb.append("/").append(name).append("/").append(name).append(".fmu");
            return Fmu.from(new File(sb.toString()));
        }

        public ModelDescriptionProvider modelDescription(String name) {
            sb.append("/").append(name).append("/").append(name).append(".fmu");
            return ModelDescriptionParser.parse(new File(sb.toString()));
        }

        public String modelDescriptionXml(String name) {
            sb.append("/").append(name).append("/").append(name).append(".fmu");
            return ModelDescriptionParser.extractModelDescriptionXml(new File(sb.toString()));
        }

        public File file(String name) {
            sb.append("/").append(name).append("/").append(name).append(".fmu");
            return new File(sb.toString());
        }

    }

}
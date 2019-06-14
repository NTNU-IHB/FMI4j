package no.ntnu.ihb.fmi4j.importer;

import no.ntnu.ihb.fmi4j.importer.fmi2.Fmu;
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionParser;
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider;
import no.ntnu.ihb.fmi4j.modeldescription.fmi2.JaxbModelDescriptionParser;

import java.io.File;
import java.io.IOException;

public class TestFMUs {

    private static String getPath() {
        return TestFMUs.class.getClassLoader().getResource("fmus").getFile();
    }


    public static FmiVersion10 fmi10() {

        return new FmiVersion10(new StringBuilder(TestFMUs.getPath()));
    }

    public static FmiVersion20 fmi20() {

        return new FmiVersion20(new StringBuilder(TestFMUs.getPath()));
    }


    public static class FmiVersion10 {

        private final StringBuilder sb;

        public FmiVersion10(StringBuilder sb) {
            this.sb = sb.append("/2.0");
        }

        public CsType cs() {
            return new CsType(sb);
        }

        public MeType me() {
            return new MeType(sb);
        }

        public BothType both() {
            return new BothType(sb);
        }

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

        public BothType both() {
            return new BothType(sb);
        }

    }


    public static class BothType {

        private final StringBuilder sb;

        public BothType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/both/").append(vendor);
            return new FmuVendor(sb);
        }

    }

    public static class CsType {

        private final StringBuilder sb;

        public CsType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/cs/").append(vendor);
            return new FmuVendor(sb);
        }

    }

    public static class MeType {

        private final StringBuilder sb;

        public MeType(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVendor vendor(String vendor) {
            sb.append("/me/").append(vendor);
            return new FmuVendor(sb);
        }

    }

    public static class FmuVendor {

        private final StringBuilder sb;

        public FmuVendor(StringBuilder sb) {
            this.sb = sb;
        }

        public FmuVersion version(String version) {
            sb.append('/').append(version);
            return new FmuVersion(sb);
        }

    }

    public static class FmuVersion {

        private final StringBuilder sb;

        public FmuVersion(StringBuilder sb) {
            this.sb = sb;
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

        public Fmu fmu() throws IOException {
            return Fmu.from(fmuFile);
        }

        public ModelDescriptionProvider modelDescription() {
            return new JaxbModelDescriptionParser().parse(fmuFile);
        }

        public String modelDescriptionXml() {
            return ModelDescriptionParser.extractModelDescriptionXml(fmuFile);
        }

        public File file() {
            return fmuFile;
        }

    }

}
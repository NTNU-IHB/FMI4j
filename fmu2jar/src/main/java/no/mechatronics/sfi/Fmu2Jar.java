package no.mechatronics.sfi;/*
 * The MIT License
 *
 * Copyright 2016 NTNU Aalesund.
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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.*;
import java.util.logging.Level;
import java.util.zip.*;
import javassist.*;
import javassist.bytecode.*;
import no.mechatronics.sfi.modeldescription.ScalarVariable;
import org.apache.commons.io.*;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.*;

/**
 * FMU to Jar converter
 *
 * @author Lars Ivar Hatledal laht@ntnu.no
 */
public class Fmu2Jar {

    private final static Logger LOG = LoggerFactory.getLogger(Fmu2Jar.class);

    private final static String PACKAGE_NAME = "no.sfi.mechatronics.fmu2jar.";

    public final CoSimulationFmu fmu;
    private final String fmuName;

    private CtClass myClass;

    /**
     * Constructor
     *
     * @param fmuPath location of FMU
     * @throws Exception
     */
    public Fmu2Jar(String fmuPath) throws Exception {
        this(fmuPath, null);
    }

    public Fmu2Jar(String fmuPath, String optionalName) throws Exception {
        this.fmu = new CoSimulationFmu(new FmuFile(new File(fmuPath)));
        this.fmuName = optionalName == null ? fmu.getModelDescription().getModelName() : optionalName;

        try {
            ClassPool pool = new ClassPool(ClassPool.getDefault());

            pool.importPackage("no.mechatronics.sfi.FmuFile");
            pool.importPackage("no.mechatronics.sfi.CoSimulationFmu");
            pool.importPackage("no.sfi.mechatronics.fmi4j.ModelDescription");
            pool.importPackage("no.sfi.mechatronics.fmi4j.ModelVariables");
            pool.importPackage("no.sfi.mechatronics.fmi4j.ScalarVariable");
            pool.importPackage("java.io");
            myClass = pool.makeClass(PACKAGE_NAME + fmuName);

            addFields();
            addConstructor();
            addCommonMethods();
            addCustomMethods();

            fmu.terminate();

        } catch (CannotCompileException ex) {
            LOG.error("Failed to create jar from fmu!", ex);
        }

    }


    /**
     * Adds fields to the generated class
     *
     * @throws CannotCompileException
     */
    private void addFields() throws CannotCompileException {
        myClass.addField(CtField.make("private final CoSimulationFmu fmu;", myClass));
    }

    /**
     * Creates the constructor
     *
     * @throws CannotCompileException
     */
    private void addConstructor() throws CannotCompileException {
        
        CtConstructor defaultConstructor = CtNewConstructor.defaultConstructor(myClass);
        try {
            CtClass ex = ClassPool.getDefault().get("java.io.IOException");
            defaultConstructor.setExceptionTypes(new CtClass[]{ex});
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        defaultConstructor.setBody(
                JtwigTemplate.classpathTemplate("templates/constructor.twig").render(JtwigModel.newModel()
                        .with("modelName", fmu.getModelDescription().getModelName()))
        );

        myClass.addConstructor(defaultConstructor);
    }
   

    /**
     * Adds common methods such as init, step, terminate etc.
     *
     * @throws CannotCompileException
     */
    private void addCommonMethods() throws CannotCompileException {

        myClass.addMethod(CtMethod.make("public double getCurrentTime() {\nreturn fmu.getCurrentTime();\n}", myClass));
        myClass.addMethod(CtMethod.make("public boolean init() {\nreturn fmu.init();\n}", myClass));
        myClass.addMethod(CtMethod.make("public void step(double stepSize) {\nreturn fmu.doStep(stepSize);\n}", myClass));
        myClass.addMethod(CtMethod.make("public void cancelStep() {\nreturn fmu.cancelStep();\n}", myClass));
        myClass.addMethod(CtMethod.make("public void terminate() {\nreturn fmu.terminate();\n}", myClass));
        myClass.addMethod(CtMethod.make("public CoSimulationFmu getFmu() {\nreturn fmu;\n}", myClass));
        myClass.addMethod(CtMethod.make("public ModelDescription getModelDescription() {\nreturn fmu.getModelDescription();\n}", myClass));
        
        try {
            myClass.addMethod(CtMethod.make(IOUtils.toString(Fmu2Jar.class.getClassLoader().getResourceAsStream("templates/readState.txt"), Charset.forName("UTF-8")), myClass));
        } catch (IOException | CannotCompileException ex) {
            java.util.logging.Logger.getLogger(Fmu2Jar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    private void addCustomMethods() throws CannotCompileException {

        for (ScalarVariable var : fmu.getModelVariables()) {

            try {

                String read = generateRead(fmu, var);
                myClass.addMethod(CtMethod.make(read, myClass));

                String write = generateWrite(fmu, var);
                myClass.addMethod(CtMethod.make(write, myClass));

            } catch (DuplicateMemberException ex) {
                LOG.warn("Duplicate variable entry: {}, ignoring...", var.getName());
            }

        }

    }


    private static String generateRead(CoSimulationFmu fmu, ScalarVariable var) {

        String varName = var.getName();
        if (!isArray(varName)) {

            JtwigTemplate classTemplate = JtwigTemplate.classpathTemplate("templates/read" + var.getTypeName() + ".twig");
            return classTemplate.render(JtwigModel.newModel()
                    .with("name1", varName)
                    .with("name2", convertName(varName))
                    .with("valueReference", var.getValueReference())
                    .with("description", var.getDescription())
                    .with("causality", var.getCausality())
                    .with("variability", var.getVariability())
                    .with("initial", var.getInitial())
                    .with("start", var.getStart())
            );

        } else {

        return "";
        }

    }

    private static String generateWrite(CoSimulationFmu fmu, ScalarVariable var) {
        
        String varName = var.getName();
        if (!isArray(varName)) {

            JtwigTemplate classTemplate = JtwigTemplate.classpathTemplate("templates/write" + var.getTypeName() + ".twig");
            return classTemplate.render(JtwigModel.newModel()
                    .with("name1", convertName(varName))
                    .with("valueReference", var.getValueReference())
            );

        } else {

            return "";
        }

    }


    public byte[] toByteCode() throws IOException, CannotCompileException {
        return myClass.toBytecode();
    }

    private static String convertName(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).replace(".", "_");
    }

    private static boolean isArray(String name) {
        return name.contains("[");
    }

    public static void convert(String source, String target) {

        if (!source.toLowerCase().endsWith(".fmu")) {
            throw new IllegalArgumentException("Source not an FMU");
        }

        if (!target.toLowerCase().endsWith(".jar")) {
            if (target.contains(".")) {
                target = target.split("\\.")[0] + ".jar";
            } else {
                target += ".jar";
            }
        }

        String fmuName = FilenameUtils.getBaseName(source);

        LOG.info("Converting FMU: {} to JAR: {}", source, target);

        try (
                FileInputStream fmuStream = new FileInputStream(source);
                JarOutputStream jos = new JarOutputStream(new FileOutputStream(target))) {
            Fmu2Jar fmu2Jar = new Fmu2Jar(source, fmuName);
            jos.putNextEntry(new ZipEntry("no/ntnu/fmi4j/fmu2jar/" + fmuName + ".class"));
            jos.write(fmu2Jar.toByteCode());
            jos.closeEntry();
            jos.putNextEntry(new ZipEntry("fmu/" + fmuName + ".fmu"));
            jos.write(IOUtils.toByteArray(fmuStream));
            jos.closeEntry();

        } catch (IOException | CannotCompileException ex) {
            java.util.logging.Logger.getLogger(Fmu2Jar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Fmu2Jar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static int getArraySize(CoSimulationFmu fmu, ScalarVariable var) {

        if (!isArray(var.getName())) {
            return 0;
        }
        String str = var.getName().split("\\[")[0];
        List<String> variableNames = fmu.getModelVariables().getVariableNames();
        return (int) variableNames.stream().filter(s -> s.contains(str)).count();
    }

    private static String convertFmuType1(ScalarVariable var) {
        switch (var.getTypeName()) {
            case "Integer": {
                return "int";
            }
            case "Real": {
                return "double";
            }
            case "String": {
                return "String";
            }
            case "Boolean": {
                return "boolean";
            }
        }
        throw new RuntimeException();
    }

    private static String convertFmuType2(ScalarVariable var) {
        switch (var.getTypeName()) {
            case "Integer": {
                return "Integer";
            }
            case "Real": {
                return "Double";
            }
            case "String": {
                return "String";
            }
            case "Boolean": {
                return "Boolean";
            }
        }
        throw new RuntimeException();
    }

}

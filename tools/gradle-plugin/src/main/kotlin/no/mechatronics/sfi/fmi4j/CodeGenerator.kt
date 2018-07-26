package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionProvider

object CodeGenerator {

    fun generateBody(md: ModelDescriptionProvider): String {

        val modelName = md.modelName
        var solverImport = ""
        if (md.supportsModelExchange) {
            solverImport = "import no.mechatronics.sfi.fmi4j.importer.me.Solver"
        }

        return """

import java.net.URL;
import java.io.File;
import no.mechatronics.sfi.fmi4j.importer.Fmu;
import no.mechatronics.sfi.fmi4j.common.FmiStatus;
import no.mechatronics.sfi.fmi4j.common.FmiSimulation;
import no.mechatronics.sfi.fmi4j.common.FmuVariableAccesor;
$solverImport

public class $modelName implements FmiSimulation {

    private FmiSimulation instance;

    private $modelName(FmiSimulation instance) {
        this.instance = instance;
    }

    ${generateFactory(md)}

    public CommonModelDescription getModelDescription() {
        return instance.getModelDescription()
    }

    public FmuVariableAccessor getVariableAccessor() {
        return instance.getVariableAccessor();
    }

    @Override
    public FmiStatus getLastStatus() {
        return instance.getLastStatus();
    }

    @Override
    public boolean isInitialized() {
        return instance.isInitialized();
    }

    @Override
    public boolean isTerminated() {
        return instance.isTerminated();
    }

    @Override
    public void init() {
        instance.init();
    }

    @Override
    public void init(double start) {
        instance.init(start);
    }

    @Override
    public void init(double start, double stop) {
        instance.init(start, stop);
    }

    public boolean doStep(double stepSize) {
        return instance.doStep(stepSize);
    }

    @Override
    public FmiStatus terminate() {
        return instance.terminate();
    }

    @Override
    public FmiStatus reset() {
        return instance.reset();
    }

}

"""

    }

    private fun generateFactory(md: ModelDescriptionProvider): String {

        var result = ""
        val className = md.modelName

        if (md.supportsCoSimulation) {
            result += """

            public static $className newInstance() {
                URL url = $className.class.getClassLoader().getResource("fmus/$className");
                File file = new File(url.toFile());
                return Fmu.from(file);
            }

            """

        }

        if (md.supportsModelExchange) {

            result += """

            public static $className newInstance(Solver solver) {
                URL url = ${className}.class.getClassLoader().getResource("fmus/$className");
                File file = new File(url.toFile());
                return Fmu.from(file);
            }
            
            """.trim()

        }

        return result

    }

}


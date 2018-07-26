package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.modeldescription.ModelDescriptionProvider
import no.mechatronics.sfi.fmi4j.modeldescription.variables.Causality
import no.mechatronics.sfi.fmi4j.modeldescription.variables.ScalarVariable

class CodeGenerator(
        private val md: ModelDescriptionProvider
) {

    private val modelName = md.modelName

    fun generateBody(): String {

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
    private ModelVariables variables;
    private CommonModelDescription modelDescription;

    private $modelName(FmiSimulation instance) {
        this.instance = instance;
        this.modelDescription = instance.getModelDescription;
        this.variables = modelDescription.getModelVariables();
    }

    ${generateFactory()}

    public CommonModelDescription getModelDescription() {
        return modelDescription;
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

    public Inputs getInputs() {
        return inputs;
    }

    private Inputs inputs = new Inputs();
    private Outputs outputs = new Outputs();
    private Parameters parameters = new Parameters();

    class AbstractParameters implements Iterable<TypedScalarVariable> {

        private final List<TypedScalarVariable> vars;

        private AbstractParameter(Causality causality) {
            this.vars = variables.getByCausality(causality);
        }

        public int size() {
            return vars.size();
        }

        public List<TypedScalarVariable> get() {
            return vars;
        }

        @Override
        public Iterator<TypedScalarVariable> getIterator() {
            return vars.iterator;
        }

    }

    class Inputs extends AbstractParameters {

        private Inputs() {
            super(Causality.INPUT)
        }

        ${generateGetters(Causality.INPUT)}
    }

    class Outputs extends AbstractParameters {

        private Outputs() {
            super(Causality.OUTPUT)
        }

        ${generateGetters(Causality.OUTPUT)}
    }

    class Parameters extends AbstractParameters {

        private Parameters() {
            super(Causality.PARAMETER)
        }

        ${generateGetters(Causality.PARAMETER)}
    }

    class CalculatedParameters extends AbstractParameters {

        private CalculatedParameters() {
            super(Causality.CALCULATED_PARAMETER)
        }

        ${generateGetters(Causality.CALCULATED_PARAMETER)}
    }

    class Locals extends AbstractParameters {

        private Locals() {
            super(Causality.LOCAL)
        }

        ${generateGetters(Causality.LOCAL)}
    }

}

"""

    }

    private fun generateFactory(): String {

        var result = ""
        if (md.supportsCoSimulation) {
            result += """

            public static $modelName newInstance() {
                URL url = $modelName.class.getClassLoader().getResource("fmus/$modelName");
                File file = new File(url.toFile());
                return Fmu.from(file);
            }

            """

        }

        if (md.supportsModelExchange) {

            result += """

            public static $modelName newInstance(Solver solver) {
                URL url = $modelName.class.getClassLoader().getResource("fmus/$modelName");
                File file = new File(url.toFile());
                return Fmu.from(file);
            }
            
            """.trim()

        }

        return result

    }

    private fun generateDoc(v: ScalarVariable): String {
        return ""
    }

    private fun generateGetters(causality: Causality): String {

        return StringBuilder().also { sb ->

            md.modelVariables.filter {
                it.causality == causality
            }.forEach {

            }

        }.toString()

    }

}


package no.mechatronics.sfi.fmi4j

import no.mechatronics.sfi.fmi4j.fmu.CoSimulationFmu
import no.mechatronics.sfi.fmi4j.fmu.Fmu
import no.mechatronics.sfi.fmi4j.fmu.ModelExchangeFmu


interface IFmuWrapper {

    fun init()

    fun step(dt: Double)

    fun terminate()

}


abstract class FmuWrapper(
       open val fmu: Fmu<*,*>
) : IFmuWrapper {

    companion object {

    }

    override fun init() {

    }

    override fun terminate() {

    }

}

class CSWrapper(
        override val fmu: CoSimulationFmu
) : FmuWrapper(fmu) {

    override fun step(dt: Double) {
        fmu.doStep(dt)
    }
}

class MEWrapper(
        override val fmu: ModelExchangeFmu
) : FmuWrapper(fmu) {

    override fun step(dt: Double) {

    }
}
package no.ntnu.ihb.fmi4j.modeldescription.jaxb

import no.ntnu.ihb.fmi4j.modeldescription.BaseUnit
import no.ntnu.ihb.fmi4j.modeldescription.DisplayUnit
import no.ntnu.ihb.fmi4j.modeldescription.Unit

class JaxbUnit internal constructor(
        private val unit: Fmi2Unit
): Unit {

    override val name: String
        get() = unit.name
    override val baseUnit: BaseUnit?
        get() = unit.baseUnit?.let { JaxbBaseUnit(it) }
    override val displayUnits: List<DisplayUnit>?
        get() = unit.displayUnit?.map { JaxbDisplayUnit(it) }
}

class JaxbBaseUnit internal constructor(
    private val baseUnit: Fmi2Unit.BaseUnit
): BaseUnit {

    override val kg: Int
        get() = baseUnit.kg
    override val m: Int
        get() = baseUnit.m
    override val s: Int
        get() = baseUnit.s
    override val A: Int
        get() = baseUnit.a
    override val K: Int
        get() = baseUnit.k
    override val mol: Int
        get() = baseUnit.mol
    override val cd: Int
        get() = baseUnit.cd
    override val rad: Int
        get() = baseUnit.rad
    override val factor: Double
        get() = baseUnit.factor
    override val offset: Double
        get() = baseUnit.offset

}

class JaxbDisplayUnit internal constructor(
    private val displayUnit: Fmi2Unit.DisplayUnit
): DisplayUnit {

    override val name: String
        get() = displayUnit.name
    override val factor: Double
        get() = displayUnit.factor
    override val offset: Double
        get() = displayUnit.offset

}

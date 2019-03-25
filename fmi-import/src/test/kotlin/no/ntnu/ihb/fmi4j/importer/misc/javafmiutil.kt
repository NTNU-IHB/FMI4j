package no.ntnu.ihb.fmi4j.importer.misc

import org.javafmi.wrapper.Simulation
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files

fun javaFmiSimulationFromUrl(url: URL): Simulation {
    val extension = File(url.file).extension
    if (extension != "fmu") {
        throw IllegalArgumentException("URL '$url' does not point to an FMU! Invalid extension found: .$extension")
    }
    val fmuName = File(url.file).nameWithoutExtension
    val temp = Files.createTempDirectory("javafmi_${fmuName}_").toFile()
    val fmuFile = File(temp, "$fmuName.fmu")
    FileOutputStream(fmuFile).use {
        it.write(url.openStream().buffered().readBytes())
        it.flush()
    }
    return Simulation(fmuFile.absolutePath).also {
        temp.deleteRecursively()
    }
}

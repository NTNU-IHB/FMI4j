package no.mechatronics.sfi.fmi4j.modeldescription

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import org.apache.commons.io.FileUtils
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.Charset

class SerializeTest {

    @Test
    fun test() {

        val path = "../test/fmi2/cs/win64/OpenModelica/v1.11.0/FmuExportCrossCompile/modelDescription.xml"
        val file = File(path)
        Assert.assertTrue(file.exists())
        val xml = FileUtils.readFileToString(file, Charset.forName("UTF-8"))
        val modelDescription = ModelDescriptionParser.parse(xml).asCoSimulationModelDescription()

        println(modelDescription.modelStructure)

        val bos = ByteOutputStream()
        ObjectOutputStream(bos).use {

            it.writeObject(modelDescription)
            it.flush()

        }

        ObjectInputStream(ByteArrayInputStream(bos.bytes)).use {
            val md: SimpleModelDescription = it.readObject() as SimpleModelDescription
            //println(md)

           // md.modelVariables.variables.forEach { println(it) }


            println(md.modelStructure)

        }


    }

}
package no.mechatronics.sfi.fmi4j.modeldescription

import no.mechatronics.sfi.fmi4j.modeldescription.cs.CoSimulationModelDescription
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.StringReader
import java.lang.IllegalArgumentException
import java.net.URL
import java.nio.charset.Charset
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.xml.bind.JAXB

private const val MODEL_DESC_FILE = "modelDescription.xml"

object ModelDescriptionParser {

    @JvmStatic
    internal fun parse(xml: String): ModelDescription = parse(xml, ModelDescriptionImpl::class.java)
    @JvmStatic
    internal fun parse(url: URL): ModelDescription = parse(url.openStream(), ModelDescriptionImpl::class.java)
    @JvmStatic
    internal fun parse(file: File): ModelDescription = parse(FileInputStream(file), ModelDescriptionImpl::class.java)
    @JvmStatic
    internal fun parse(inputStream: InputStream): ModelDescription = parse(inputStream, ModelDescriptionImpl::class.java)

    internal fun <T: ModelDescriptionImpl> parse(xml: String, type: Class<T>): T = JAXB.unmarshal(StringReader(xml), type)
    internal fun <T : ModelDescriptionImpl> parse(stream: InputStream, type: Class<T>): T = exctractModelDescriptionXml(stream).let { parse(it, type) }

    @JvmStatic
    fun exctractModelDescriptionXml(stream: InputStream): String {

        ZipInputStream(stream).use {

            var nextEntry: ZipEntry? = it.nextEntry
            while (nextEntry != null) {

                if (nextEntry.name == MODEL_DESC_FILE) {
                    return IOUtils.toString(it, Charset.forName("UTF-8"))
                }

                nextEntry = it.nextEntry
            }

        }

        throw IllegalArgumentException("Input is not an valid FMU! No $MODEL_DESC_FILE present!")

    }

}



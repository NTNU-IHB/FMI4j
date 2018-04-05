package no.mechatronics.sfi.fmi4j.modeldescription

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.StringReader
import java.lang.IllegalArgumentException
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.xml.bind.JAXB

private const val MODEL_DESC_FILE = "modelDescription.xml"

object ModelDescriptionParser {

    @JvmStatic
    fun parse(url: URL): ModelDescriptionProvider = parse(url.openStream())

    @JvmStatic
    fun parse(file: File): ModelDescriptionProvider = parse(FileInputStream(file))

    @JvmStatic
    fun parse(xml: String): ModelDescriptionProvider = JAXB.unmarshal(StringReader(xml), ModelDescriptionImpl::class.java)

    @JvmStatic
    private fun parse(stream: InputStream): ModelDescriptionProvider = extractModelDescriptionXml(stream).let (::parse)

    @JvmStatic
    fun extractModelDescriptionXml(stream: InputStream): String {
        ZipInputStream(stream).use {
            var nextEntry: ZipEntry? = it.nextEntry
            while (nextEntry != null) {
                if (nextEntry.name == MODEL_DESC_FILE) {
                    return it.bufferedReader(Charsets.UTF_8).use { it.readText() }
                }
                nextEntry = it.nextEntry
            }
        }
        throw IllegalArgumentException("Input is not an valid FMU! No $MODEL_DESC_FILE present!")
    }

}
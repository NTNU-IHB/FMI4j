/*
 * The MIT License
 *
 * Copyright 2017-2018 Norwegian University of Technology
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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING  FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package no.ntnu.ihb.fmi4j.modeldescription.parser

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionImpl
import no.ntnu.ihb.fmi4j.modeldescription.ModelDescriptionProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


/**
 * Parses the modelDescription.xml holding static information about an FMU
 */
object ModelDescriptionParser {

    private const val MODEL_DESC_FILE = "modelDescription.xml"

    private val mapper by lazy {
        XmlMapper().apply {
            registerModule(KotlinModule())
            registerModule(JacksonXmlModule())
            enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        }
    }

    @JvmStatic
    fun parse(url: URL): ModelDescriptionProvider {
        return parse(url.openStream())
    }

    @JvmStatic
    fun parse(file: File): ModelDescriptionProvider {
        if (!file.exists()) {
            throw FileNotFoundException("No such file '${file.absolutePath}'!")
        }
        return parse(FileInputStream(file))
    }

    @JvmStatic
    fun parse(xml: String): ModelDescriptionProvider {

        val correctedXml = xml.replace("calculatedParameter", "CALCULATED_PARAMETER")
                .replace("<ModelStructure>\n" +
                        "</ModelStructure>", "<ModelStructure/>")

        return mapper.readValue<ModelDescriptionImpl>(correctedXml)

    }

    @JvmStatic
    private fun parse(stream: InputStream): ModelDescriptionProvider {
        return parse(extractModelDescriptionXml(stream))
    }

    @JvmStatic
    fun extractModelDescriptionXml(url: URL): String {
        return url.openStream().use { extractModelDescriptionXml(it) }
    }

    @JvmStatic
    fun extractModelDescriptionXml(file: File): String {
        return file.inputStream().use { extractModelDescriptionXml(it) }
    }

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


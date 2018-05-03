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

package no.mechatronics.sfi.fmi4j.modeldescription

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.StringReader
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.xml.bind.JAXB


private const val MODEL_DESC_FILE = "modelDescription.xml"

/**
 * Parses the modelDescription.xml holding static information about an FMU
 */
object ModelDescriptionParser {

    @JvmStatic
    fun parse(url: URL): ModelDescriptionProvider {
        return parse(url.openStream())
    }

    @JvmStatic
    fun parse(file: File): ModelDescriptionProvider {
        return parse(FileInputStream(file))
    }

    @JvmStatic
    fun parse(xml: String): ModelDescriptionProvider {
        return JAXB.unmarshal(StringReader(xml), ModelDescriptionImpl::class.java)
    }

    @JvmStatic
    private fun parse(stream: InputStream): ModelDescriptionProvider {
        return parse(extractModelDescriptionXml(stream))
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
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

package no.ntnu.ihb.fmi4j.modeldescription

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


abstract class ModelDescriptionParser {

    abstract fun parse(xml: String): ModelDescriptionProvider

    fun parse(url: URL): ModelDescriptionProvider {
        return parse(url.openStream())
    }

    fun parse(file: File): ModelDescriptionProvider {
        if (!file.exists()) {
            throw FileNotFoundException("No such file '${file.absolutePath}'!")
        }
        return parse(FileInputStream(file))
    }

    fun parse(stream: InputStream): ModelDescriptionProvider {
        return parse(extractModelDescriptionXml(stream))
    }

    companion object {

        private const val MODEL_DESC_FILE = "modelDescription.xml"

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
            ZipInputStream(stream).use { zis ->
                var nextEntry: ZipEntry? = zis.nextEntry
                while (nextEntry != null) {
                    if (nextEntry.name == MODEL_DESC_FILE) {
                        return zis.bufferedReader(Charsets.UTF_8).use { it.readText() }
                    }
                    nextEntry = zis.nextEntry
                }
            }
            throw IllegalArgumentException("Input is not an valid FMU! No $MODEL_DESC_FILE present!")
        }


    }

}

/*
 * The MIT License
 *
 * Copyright 2017-2019 Norwegian University of Technology
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

package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.util.OsUtil
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicBoolean

internal class FMI4j {

    companion object {

        private val initialized = AtomicBoolean(false)
        private val fileName = "${OsUtil.libPrefix}fmi4j-import.${OsUtil.libExtension}"

        fun init() {
            if (!initialized.getAndSet(true)) {

                val tempFolder = Files.createTempDirectory("fmi4j_dll").toFile()
                val fmi4jdll = File(tempFolder, fileName)
                try {
                    val resourceName = "native/fmi/${OsUtil.currentOS}/$fileName"
                    FMI4j::class.java.classLoader
                            .getResourceAsStream(resourceName)?.use { `is` ->
                                FileOutputStream(fmi4jdll).use { fos ->
                                    `is`.copyTo(fos)
                                }
                            } ?: throw IllegalStateException("NO such resource '$resourceName'!")
                    System.load(fmi4jdll.absolutePath)
                } catch (ex: Exception) {
                    tempFolder.deleteRecursively()
                    throw RuntimeException(ex)
                } finally {
                    fmi4jdll.deleteOnExit()
                    tempFolder.deleteOnExit()
                }
            }
        }

    }

}

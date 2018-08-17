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

package no.mechatronics.sfi.fmi4j.importer.jni

import java.io.IOException
import java.util.HashMap

/**
 * A simple class loader
 */
class SimpleClassLoader : ClassLoader() {

    companion object {
        private val classes = HashMap<String, Class<*>>()
    }

    /**
     * The HashMap where the classes will be cached
     */


    @Throws(ClassNotFoundException::class)
    public override fun findClass(name: String): Class<*>? {

        if (classes.containsKey(name)) {
            return classes[name]
        }

        val classData: ByteArray

        try {
            classData = loadClassData(name)
        } catch (e: IOException) {
            throw ClassNotFoundException("Class [$name] could not be found", e)
        }

        return defineClass(name, classData, 0, classData.size).also {
            resolveClass(it)
            classes[name] = it
        }

    }

    @Throws(IOException::class)
    private fun loadClassData(name: String): ByteArray {
        return ClassLoader.getSystemResourceAsStream(name.replace(".", "/") + ".class")
                .buffered().use { `in` -> `in`.readBytes() }
    }

    override fun toString(): String {
        return SimpleClassLoader::class.java.name
    }

}
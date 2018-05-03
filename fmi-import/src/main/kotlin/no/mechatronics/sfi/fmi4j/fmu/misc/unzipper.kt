package no.mechatronics.sfi.fmi4j.fmu.misc

import java.io.File
import java.io.InputStream
import java.net.URL
import java.util.zip.ZipInputStream


private fun extractTo(directory: File, inputStream: InputStream) {

    ZipInputStream(inputStream).use { zis ->
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            if (!zipEntry.isDirectory) {
                val child = File(directory, zipEntry.name).also {
                    if (!it.parentFile.exists()) {
                        it.parentFile.mkdirs()
                    }
                    it.createNewFile()
                }
                child.writeBytes(zis.readBytes())
            }
            zis.closeEntry()
            zipEntry = zis.nextEntry
        }
    }

}

fun File.extractTo(directory: File) {
   extractTo(directory, inputStream())
}

fun URL.extractTo(directory: File) {
   extractTo(directory, openStream())
}

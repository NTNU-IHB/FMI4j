//package no.mechatronics.sfi.fmi4j.fmu.misc
//
//import no.mechatronics.sfi.fmi4j.fmu.FMI4J_FILE_PREFIX
//import org.apache.commons.io.FilenameUtilsLite
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import java.io.File
//import java.net.URL
//import java.nio.file.Files
//
//
//class ExctractedFmuFolder private constructor(
//        val source: FmuSource
//) {
//
//
//    var numReferences = 0
//        private set
//
//    fun reference() {
//        if (numReferences == 0) {
//            source.extract()
//        }
//        numReferences++
//    }
//
//    fun unreference() {
//        numReferences--
//        if (numReferences == 0) {
//
//        }
//    }
//
//    companion object {
//
//        private val LOG: Logger = LoggerFactory.getLogger(ExctractedFmuFolder::class.java)
//
//        fun extract(zippedFmuFile: URL) {
//            val baseName = FilenameUtilsLite.getBaseName(zippedFmuFile.toString())
//            val tmp = Files.createTempFile(FMI4J_FILE_PREFIX + baseName, ".fmuInstance").toFile()
//            zippedFmuFile.readBytes().also { data ->
//                tmp.writeBytes(data)
//            }
//
//            LOG.debug("Copied fmuInstance from url into $tmp")
//            val extractToTempFolder = extractFmuToTempFolder(tmp)
//
//            if (tmp.delete()) {
//                LOG.debug("Deleted temp fmuInstance file retrieved from url $tmp")
//            }
//        }
//
//        fun extract(zippedFmuFile: File): ExctractedFmuFolder {
//
//            val baseName = FilenameUtilsLite.getBaseName(zippedFmuFile.toString())
//            val tmp = Files.createTempFile(FMI4J_FILE_PREFIX + baseName, ".fmuInstance").toFile()
//            zippedFmuFile.readBytes().also { data ->
//                tmp.writeBytes(data)
//            }
//
//            return ExctractedFmuFolder(tmp)
//
//        }
//
//    }
//
//}
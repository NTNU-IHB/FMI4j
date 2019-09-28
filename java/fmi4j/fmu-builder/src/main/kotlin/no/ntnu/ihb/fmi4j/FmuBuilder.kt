package no.ntnu.ihb.fmi4j

import no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription
import picocli.CommandLine
import java.io.*
import java.net.URLClassLoader
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.xml.bind.JAXB

object FmuBuilder {

    @CommandLine.Command(name = "fmu-builder")
    class Args : Runnable {

        @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
        var showHelp = false

        @CommandLine.Option(names = ["-f", "--file"], description = ["Path to the Jar."], required = true)
        lateinit var jarFile: File

        @CommandLine.Option(names = ["-m", "--main"], description = ["Full qualified name if the main class."], required = true)
        lateinit var mainClass: String

        override fun run() {

            require(jarFile.name.endsWith(".jar")) { "File $jarFile is not a .jar!" }

            URLClassLoader(arrayOf(jarFile.toURI().toURL()))

            val superClass = Class.forName("no.ntnu.ihb.fmi4j.Fmi2Slave")
            val subClass = Class.forName(mainClass) ?: throw IllegalArgumentException("Unable to find class $mainClass!")
            val instance = subClass.newInstance()

            val define = superClass.getDeclaredMethod("define")
            define.invoke(instance)
            val getModelDescription = superClass.getDeclaredMethod("getModelDescription")
            val md = getModelDescription.invoke(instance) as Fmi2ModelDescription
            val modelIdentifier = md.coSimulation.modelIdentifier

            val bos = ByteArrayOutputStream()
            JAXB.marshal(md, bos)

            ZipOutputStream(BufferedOutputStream(FileOutputStream("${modelIdentifier}.fmu"))).use { zos ->

                zos.putNextEntry(ZipEntry("modelDescription.xml"))
                zos.write(bos.toByteArray())
                zos.closeEntry()

                zos.putNextEntry(ZipEntry("resources/"))
                zos.putNextEntry(ZipEntry("resources/model.jar"))

                FileInputStream(jarFile).use { fis ->
                    fis.buffered().copyTo(zos)
                }
                zos.closeEntry()

                zos.putNextEntry(ZipEntry("resources/mainclass.txt"))
                zos.write(mainClass.toByteArray())
                zos.closeEntry()


                zos.putNextEntry(ZipEntry("binaries/"))

                zos.putNextEntry(ZipEntry("binaries/win64/$modelIdentifier.dll"))
                FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/win/fmi4j-export.dll")?.use { `is` ->
                    `is`.buffered().copyTo(zos)
                }
                zos.closeEntry()

                zos.putNextEntry(ZipEntry("binaries/linux64/$modelIdentifier.so"))
                FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/linux/fmi4j-export.dll")?.use { `is` ->
                    `is`.buffered().copyTo(zos)
                }
                zos.closeEntry()

            }

        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        CommandLine.run(Args(), System.out, *args)
    }

}
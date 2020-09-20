package no.ntnu.ihb.fmi4j

import picocli.CommandLine
import java.io.*
import java.net.URLClassLoader
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.xml.bind.JAXB

class FmuBuilder(
        private val mainClass: String,
        private val jarFile: File,
        private val resources: Array<File>?
) {

    @JvmOverloads
    fun build(dest: File? = null) {

        require(jarFile.exists()) { "No such File '$jarFile'" }
        require(jarFile.name.endsWith(".jar")) { "File $jarFile is not a .jar!" }

        val classLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()))

        val superClass = classLoader.loadClass("no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave")
        val subClass = classLoader.loadClass(mainClass)
        val instance = subClass.getConstructor(Map::class.java).newInstance(mapOf("instanceName" to "dummyInstance"))

        val mdClass = classLoader.loadClass("no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription")
        val mdCsClass = classLoader.loadClass("no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription\$CoSimulation")
        val mdCsMethod = mdClass.getMethod("getCoSimulation")
        val mdCsModelIdentifierMethod = mdCsClass.getMethod("getModelIdentifier")

        val define = superClass.getDeclaredMethod("__define__")
        define.isAccessible = true
        define.invoke(instance)
        val getModelDescription = superClass.getDeclaredMethod("getModelDescription")
        getModelDescription.isAccessible = true
        val md = getModelDescription.invoke(instance)
        val mdCs = mdCsMethod.invoke(md)
        val modelIdentifier = mdCsModelIdentifierMethod.invoke(mdCs) as String

        val bos = ByteArrayOutputStream()
        JAXB.marshal(md, bos)

        val destDir = dest ?: File(".")
        val destFile = File(destDir, "${modelIdentifier}.fmu").apply {
            if (!exists()) {
                parentFile.mkdirs()
                createNewFile()
            }
        }

        ZipOutputStream(BufferedOutputStream(FileOutputStream(destFile))).use { zos ->

            zos.putNextEntry(ZipEntry("modelDescription.xml"))
            zos.write(bos.toByteArray())
            zos.closeEntry()

            zos.putNextEntry(ZipEntry("resources/"))
            zos.putNextEntry(ZipEntry("resources/model.jar"))

            FileInputStream(jarFile).buffered().use { fis ->
                zos.write(fis.readBytes())
            }
            zos.closeEntry()

            resources?.forEach { file ->
                FileInputStream(file).buffered().use {
                    zos.putNextEntry(ZipEntry("resources/${file.name}"))
                    zos.write(it.readBytes())
                    zos.closeEntry()
                }
            }

            zos.putNextEntry(ZipEntry("resources/mainclass.txt"))
            zos.write(mainClass.toByteArray())
            zos.closeEntry()


            zos.putNextEntry(ZipEntry("binaries/"))

            FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/win32/fmi4j-export.dll")?.use { `is` ->
                zos.putNextEntry(ZipEntry("binaries/win32/"))
                zos.putNextEntry(ZipEntry("binaries/win32/$modelIdentifier.dll"))
                zos.write(`is`.readBytes())
                zos.closeEntry()
            }

            FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/win64/fmi4j-export.dll")?.buffered()?.use { `is` ->
                zos.putNextEntry(ZipEntry("binaries/win64/"))
                zos.putNextEntry(ZipEntry("binaries/win64/$modelIdentifier.dll"))
                zos.write(`is`.readBytes())
                zos.closeEntry()
            }

            FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/linux64/libfmi4j-export.so")?.buffered()?.use { `is` ->
                zos.putNextEntry(ZipEntry("binaries/linux64/"))
                zos.putNextEntry(ZipEntry("binaries/linux64/$modelIdentifier.so"))
                zos.write(`is`.readBytes())
                zos.closeEntry()
            }

        }

    }

    @CommandLine.Command(name = "fmu-builder")
    class Args : Runnable {

        @CommandLine.Option(names = ["-h", "--help"],
                description = ["Print this message and quits."], usageHelp = true)
        var showHelp = false

        @CommandLine.Option(names = ["-m", "--main"], description = ["Fully qualified name of the main class."], required = true)
        lateinit var mainClass: String

        @CommandLine.Option(names = ["-f", "--file"], description = ["Path to the Jar."], required = true)
        lateinit var jarFile: File

        @CommandLine.Option(names = ["-d", "--dest"], description = ["Where to save the FMU."], required = false)
        var destFile: File? = null

        @CommandLine.Option(names = ["-r", "--res"], arity= "0..*", description = ["resources."], required = false)
        var resources: Array<File>? = null

        override fun run() {
            FmuBuilder(mainClass, jarFile, resources).build(destFile)
        }

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            CommandLine(Args()).execute(*args)
        }
    }

}

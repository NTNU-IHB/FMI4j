package no.ntnu.ihb.fmi4j

import picocli.CommandLine
import java.io.*
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.xml.bind.JAXB

private const val DUMMY_INSTANCE_NAME = "dummyInstance"

class FmuBuilder(
        private val mainClass: String,
        private val jarFile: File,
        private val resources: Array<File>?
) {

    @JvmOverloads
    fun build(dest: File? = null): File {

        require(jarFile.exists()) { "No such File '$jarFile'" }
        require(jarFile.name.endsWith(".jar")) { "File $jarFile is not a .jar!" }

        var tempResourcesDir: File? = null

        val fmuArgs = mutableMapOf<String, Any>("instanceName" to DUMMY_INSTANCE_NAME)
        if (resources != null) {
            tempResourcesDir = Files.createTempDirectory("fmu-resources").toFile()
            for (file in resources) {
                if (file.isDirectory) {
                    file.copyRecursively(tempResourcesDir)
                } else {
                    file.copyTo(File(tempResourcesDir, file.name))
                }
            }
            fmuArgs["resourceLocation"] = tempResourcesDir.absolutePath
        }

        val classLoader = URLClassLoader(arrayOf(jarFile.toURI().toURL()))

        val superClass = classLoader.loadClass("no.ntnu.ihb.fmi4j.export.fmi2.Fmi2Slave")
        val subClass = classLoader.loadClass(mainClass)
        val instance = subClass.getConstructor(Map::class.java).newInstance(fmuArgs)

        val mdClass = classLoader.loadClass("no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription")
        val mdCsClass = classLoader.loadClass("no.ntnu.ihb.fmi4j.modeldescription.fmi2.Fmi2ModelDescription\$CoSimulation")
        val mdCsMethod = mdClass.getMethod("getCoSimulation")
        val mdCsModelIdentifierMethod = mdCsClass.getMethod("getModelIdentifier")

        val define = superClass.getDeclaredMethod("__define__")
        define.invoke(instance)

        val getModelDescription = superClass.getDeclaredMethod("getModelDescription")
        val md = getModelDescription.invoke(instance)
        val mdCs = mdCsMethod.invoke(md)
        val modelIdentifier = mdCsModelIdentifierMethod.invoke(mdCs) as String

        val close = superClass.getDeclaredMethod("close")
        close.invoke(instance)

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
                zos.closeEntry()
            }

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

            zos.closeEntry() //resources

            zos.putNextEntry(ZipEntry("binaries/"))

            FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/win32/fmi4j-export.dll")?.use { `is` ->
                zos.putNextEntry(ZipEntry("binaries/win32/"))
                zos.putNextEntry(ZipEntry("binaries/win32/$modelIdentifier.dll"))
                zos.write(`is`.readBytes())
                zos.closeEntry()
                zos.closeEntry()
            }

            FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/win64/fmi4j-export.dll")?.buffered()?.use { `is` ->
                zos.putNextEntry(ZipEntry("binaries/win64/"))
                zos.putNextEntry(ZipEntry("binaries/win64/$modelIdentifier.dll"))
                zos.write(`is`.readBytes())
                zos.closeEntry()
                zos.closeEntry()
            }

            FmuBuilder::class.java.classLoader.getResourceAsStream("binaries/linux64/libfmi4j-export.so")?.buffered()?.use { `is` ->
                zos.putNextEntry(ZipEntry("binaries/linux64/"))
                zos.putNextEntry(ZipEntry("binaries/linux64/$modelIdentifier.so"))
                zos.write(`is`.readBytes())
                zos.closeEntry()
                zos.closeEntry()
            }

            zos.closeEntry() //binaries

        }

        tempResourcesDir?.also {
            it.deleteRecursively()
        }

        return destFile

    }

    @CommandLine.Command(name = "fmu-builder")
    class Args : Runnable {

        @CommandLine.Option(names = ["-h", "--help"], description = ["Print this message and quits."], usageHelp = true)
        var showHelp = false

        @CommandLine.Option(names = ["-m", "--main"], description = ["Fully qualified name of the main class."], required = true)
        lateinit var mainClass: String

        @CommandLine.Option(names = ["-f", "--file"], description = ["Path to the Jar."], required = true)
        lateinit var jarFile: File

        @CommandLine.Option(names = ["-d", "--dest"], description = ["Where to save the FMU."], required = false)
        var destFile: File? = null

        @CommandLine.Option(names = ["-r", "--res"], arity = "0..*", description = ["resources."], required = false)
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

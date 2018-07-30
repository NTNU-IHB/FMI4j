# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/SFI-Mechatronics/FMI4j/issues)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import)


FMI4j is a software package for dealing with Functional Mock-up Units (FMUs) on the Java Virtual Machine (JVM), written in [Kotlin](https://kotlinlang.org/). 

The package consists of:
* [A software API for interacting with FMUs](#api)
* [A Gradle Plugin that makes it easier to progamatically work with FMUs](#plugin)
* [A tool for wrapping an FMU as a JAR - batteries included](#fmu2jar).

FMI4j supports [FMI](http://fmi-standard.org/) 2.0 for **Model Exchange** and **Co-simulation**.
For Model Exchange, solvers from [Apache Commons Math](http://commons.apache.org/proper/commons-math/userguide/ode.html) can be used.

To get started head over to the [Wiki](https://github.com/SFI-Mechatronics/FMI4j/wiki)!


### <a name="api"></a> Software API 

```java

Fmu fmu = Fmu.from(new File("path/to/fmu.fmu)); //URLs are also supported

FmiSimulation instance = fmu.asCoSimulationFmu().newInstance();

// Model Exchange is also supported:
//
// Solver solver = ApacheSolvers.euler(1E-3);
// FmiSimulation instance = fmu.asModelExchangeFmu(solver).newInstance(); 

instance.init(); //throws on error

double stop = 10;
double stepSize = 1.0/100;
while(instance.getCurrentTime() <= stop) {
    instance.doStep(stepSize);
}
instance.terminate(); //or close

fmu.close() // <- also done automatically by the library if you forget to do it yourself

```

### <a name="plugin"></a> Gradle plugin

For any FMUs located in your ```resources/fmus``` folder, the plugin generates Java code which makes it easier to interact with them progamatically.

Among other things, it generates type safe getter and setters for the FMU variables - grouped by causality. 
It also generates javadoc based on the information found in the ```modelDescription.xml```.

Example:

```kotlin

ControlledTemperature.newInstance().use { instance ->

        instance.init()
        
        //Variables are grouped by causality and have types!
        val tempRef: RealVariable 
                = instance.outputs.temperature_Reference()

        val stop = 10.0
        val stepSize = 1E-2
        while (instance.currentTime <= stop) {
            
            if (!instance.doStep(stepSize)) {
                break;
            }

            val read = tempRef.read()
            println("t=${instance.currentTime}, ${tempRef.name}=${read.value}")
            
        }

    }

```

The plugin has been added to the [Gradle Plugin portal](https://plugins.gradle.org/plugin/no.mechatronics.sfi.fmi4j.FmuPlugin).

To use it, simply add the following to your build.gradle

```gradle
plugins {
    id "no.mechatronics.sfi.fmi4j.FmuPlugin" version "0.1"
}

```

and add a dependency to the latest version of ```fmi-import```

```gradle
compile group: "no.mechatronics.sfi.fmi4j", name: 'fmi-import', version: '0.8'
```

### <a name="fmu2jar"></a> FMU2Jar

FMU2Jar is similar to the Gradle-plugin, but you can use it without Gradle. 

It is a command line application which takes an FMU in and produces a Jar file. 
You can also tell the application to install the Jar into your local maven repository (``.m2``),
The jar file contains code that makes it easier to work with the FMU progamatically just as the Gradle plugin does. 

#### Running tests

In order to run the tests, a system variable named __TEST_FMUs__ must be present on your system. 
This variable should point to the location of the content found [here](https://github.com/markaren/TEST_FMUs).

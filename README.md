# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/SFI-Mechatronics/FMI4j/issues)

[![](https://jitpack.io/v/NTNU-IHB/FMI4j.svg)](https://jitpack.io/#NTNU-IHB/FMI4j) 
[![Gitter](https://badges.gitter.im/NTNU-IHB/FMI4j.svg)](https://gitter.im/NTNU-IHB/FMI4j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


[![CircleCI](https://circleci.com/gh/NTNU-IHB/FMI4j/tree/master.svg?style=svg)](https://circleci.com/gh/NTNU-IHB/FMI4j/tree/master)
[![Build Status](https://dev.azure.com/laht/laht/_apis/build/status/NTNU-IHB.FMI4j?branchName=master)](https://dev.azure.com/laht/laht/_build/latest?definitionId=2&branchName=master)


FMI4j is a software package for dealing with Functional Mock-up Units (FMUs) on the Java Virtual Machine (JVM), written in [Kotlin](https://kotlinlang.org/). 

FMI4j supports [FMI](http://fmi-standard.org/) 2.0 for **Model Exchange** and **Co-simulation**. <br/>
For Model Exchange, solvers from [Apache Commons Math](http://commons.apache.org/proper/commons-math/userguide/ode.html) can be used.

Compared to other FMI libraries targeting the JVM, FMI4j is **considerably faster** due to the fact that we use JNI with manually optimized C++-code instead of JNA or SWIG generated bindings. 
A significant speedup (2-5x) compared to other FMI implementations for the JVM, such as JFMI and JavaFMI, should be expected. 

The package consists of:
* [A software API for interacting with FMUs](#api)
* [A Gradle Plugin that makes it easier to programmatically work with FMUs](#plugin)
* [A tool for wrapping an FMU as a JAR - batteries included](#fmu2jar).
* [A tool for running FMUs from the command line](#fmudriver).


### <a name="api"></a> Software API 

```java

class Demo {
    
    void main(String[] args) {
        
        Fmu fmu = Fmu.from(new File("path/to/fmu.fmu")); //URLs are also supported
        FmuSlave slave = fmu.asCoSimulationFmu().newInstance();
        
        // Model Exchange is also supported:
        //
        // Solver solver = ApacheSolvers.euler(1E-3);
        // FmuSlave slave = fmu.asModelExchangeFmu(solver).newInstance(); 
        
        slave.simpleSetup();
        
        double stop = 10;
        double stepSize = 1.0/100;
        while(slave.getSimulationTime() <= stop) {
            if (!slave.doStep(stepSize)) {
                break;
            }
        }
        slave.terminate(); //or close, try with resources is also supported
        fmu.close(); // <- also done automatically by the library if you forget to do it yourself
        
    }
    
}
```

Due to how cumbersome it is to publish artifacts to Maven Central, new releases can only be obtained using __jitpack__.

```groovy
allprojects {
    repositories {
        /*...*/
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    def fmi4j_version = "..." // e.g 0.16.0
    implementation "com.github.NTNU-IHB.FMI4j:fmi-import:${fmi4j_version}"
}
```

### <a name="plugin"></a> Gradle plugin

For any FMUs located in your ```resources/fmus``` folder, the plugin generates Java code which makes it easier to interact with them progamatically.

Among other things, it generates type safe getter and setters for the FMU variables - grouped by causality. 
It also generates javadoc based on the information found in the ```modelDescription.xml```.

Example for an FMU named _ControlledTemperature_ given in Kotlin:

```kotlin
ControlledTemperature.newInstance().use { slave -> //try with resources

        slave.simpleSetup()
        
        //Variables are grouped by causality and have types!
        val tempRef: RealVariable 
                = slave.outputs.temperature_Reference()

        val stop = 10.0
        val stepSize = 1E-2
        while (slave.simulationTime <= stop) {
            
            if (!slave.doStep(stepSize)) {
                break
            }

            tempRef.read(slave).also {
                println("t=${instance.currentTime}, ${tempRef.name}=${it.value}")
            }
            
        }

    }
```

The plugin has been added to the [Gradle Plugin portal](https://plugins.gradle.org/plugin/no.ntnu.ihb.fmi4j.FmuPlugin).

To use it, simply add the following to your ```build.gradle```

```groovy
plugins {
    id "no.ntnu.ihb.fmi4j.FmuPlugin" version "..."
}
```

and the following to your ``settings.gradle``

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url 'https://jitpack.io' }
    }
}
```

The plugin will automatically add a dependency to FMI4j. It defaults to the _implementation_ configuration. You can change this behaviour through the _fmi4j_ extension. E.g:

```groovy
fmi4j {
    version = "..."
    configurationName = "compile"
}
```

### <a name="fmu2jar"></a> FMU2Jar

FMU2Jar is similar to the Gradle plugin, but it does not require Gradle. 

It's a command line application which takes an FMU in and produces a Jar file. 
You can also tell the application to install the Jar into your local maven repository (``.m2``),
The jar file contains code that makes it easier to work with the FMU programmatically just as the Gradle plugin does. 


### <a name="fmudriver"></a> FmuDriver

FmuDriver is a command line tool for running FMUs. Run: 

```bash
java -jar FmuDriver -h
```

to see available options.


___

To get started head over to the [Wiki](https://github.com/NTNU-IHB/FMI4j/wiki)!


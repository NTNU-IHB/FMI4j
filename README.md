# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/SFI-Mechatronics/FMI4j/issues)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import)


FMI4j is a software library for dealing with Functional Mock-up Units (FMUs) on the Java Virtual Machine (JVM), written in [Kotlin](https://kotlinlang.org/). 


FMI4j supports [FMI](http://fmi-standard.org/) 2.0 for **Model Exchange** and **Co-simulation**.
For Model Exchange, solvers from [Apache Commons Math](http://commons.apache.org/proper/commons-math/userguide/ode.html) are also included.


To get started head over to the [Wiki](https://github.com/SFI-Mechatronics/FMI4j/wiki)!


#### Running tests

In order to run the tests, a system variable named __TEST_FMUs__ must be present on your system. This variable should point to the location of the content found [here](https://github.com/markaren/TEST_FMUs).
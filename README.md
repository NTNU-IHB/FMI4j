# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import)


FMI4j is a software library for dealing with Functional Mock-up Units in Kotlin/Java.
Currently it consists of two parts:

## FMI Import

Allows FMUs for Model Exhange and Co-simulation (version 2.0 only) to be imported in Java applications.
For Model Exchange, solvers are also included


#### Co-simulation example

```java

Fmi2Simulation fmu = new CoSimulationFmu(new FmuFilenew File("path/to/fmu.fmu")));
fmu.init();

double dt = 1d/100;
while (fmu.getCurrentTime() < 10) {
    fmu.doStep(dt);
}

fmu.terminate();

```


#### Model-exchange example

```java

FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);
Fmi2Simulation fmu = new ModelExchangeFmuWithIntegrator(new FmuFile(new File("path/to/fmu.fmu")), integrator);
fmu.init();

double dt = 1d/100;
while (fmu.getCurrentTime() < 5) {
    fmu.step(dt);
}

fmu.terminate();

```

## FMU2Jar

Command line tool for packaging an FMU into a Java library. This allows you to use the FMU as any other Java library. 

### Usage

fmi2jar -fmu "fmu/location.fmu" -outputFolder "where/to/put/generated/jar"

add -mavenLocal if you want the .jar to be installed in your local maven repository
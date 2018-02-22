# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import)


FMI4j is a software library for dealing with Functional Mock-up Units in Kotlin/Java.

## FMI Import

Allows FMUs for Model Exchange and Co-simulation (version 2.0 only) to be imported in Java applications.
For Model Exchange, solvers are also included


#### Co-simulation example

##### Java API
```java
FMUBuilder builder = new FmuBuilder(new File("path/to/fmu.fmu"));
try(FmiSimulation fmu = builder.asCoSimulationFmu().newInstance()) {

    RealVariable myVar = fmu.getVariableByName("myVar").asRealVariable()

    //assign custom start values
    myVar.setStart(2d);
                
    if (fmu.init()) {
        
        double myValue = myVar.read().getValue(); //read
        FmiStatus status = myVar.write(5d); //write
        
        double dt = 1d/100;
        while (fmu.getCurrentTime() < 10) {
            fmu.doStep(dt);
        }
    }
} //fmu is terminated
```

##### Kotlin API

```kotlin
val builder = FmuBuilder(File("path/to/fmu.fmu"))
builder.asCoSimulationFmu().newInstance().use { fmu -> 

    val myVar =  fmu.getVariableByName("myVar").asRealVariable()

    //assign custom start values
    myVar.start = 2.0
    
    if (fmu.init()) {
    
        val myVal = myVar.read().value //read
        val status = myVar.write(5.0) //write
    
        val dt = 1.0/100
        while (fmu.currentTime < 10.0) {
            fmu.doStep(dt);
        }
    }
    
} //fmu is terminated
```

#### Model Exchange (with integrator) example

##### Java API
```java
FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);
FMUBuilder builder = new FmuBuilder(new File("path/to/fmu.fmu"));
try (FmiSimulation fmu = builder.asModelExchangeFmu()
                        .newInstance(integrator)) {

    //assign custom start values
    ...

    if (fmu.init()) {
       double dt = 1d/100;
       while (fmu.getCurrentTime() < 5) {
           fmu.step(dt);
       } 
    }
    
} //fmu is terminated
```

##### Kotlin API
```kotlin
val integrator = ClassicalRungeKuttaIntegrator(1E-3)
val builder = FmuBuilder(File("path/to/fmu.fmu"))
builder.asModelExchangeFmu()
        .newInstance(integrator).use { fmu -> 
        
            //assign custom start values
            ...
        
            if (fmu.init()) {
                val dt = 1.0/100;
                while (fmu.currentTime < 5) {
                    fmu.step(dt)
                }
            }

        } //fmu is terminated
```

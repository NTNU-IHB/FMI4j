# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import)


FMI4j is a software library for dealing with Functional Mock-up Units in Kotlin/Java.
Currently it consists of two parts:

## FMI Import

Allows FMUs for Model Exchange and Co-simulation (version 2.0 only) to be imported in Java applications.
For Model Exchange, solvers are also included


#### Co-simulation example

##### Java API
```java
FMUBuilder builder = new FmuBuilder(new File("path/to/fmu.fmu"));
try(FmiSimulation fmu = builder.asCoSimulationFmu().newInstance()) {

    //set start values
    
    if (fmu.init()) {
        double dt = 1d/100;
        while (fmu.getCurrentTime() < 10) {
            fmu.doStep(dt);
        }
    }
}
```

##### Kotlin API

```kotlin
val builder = FmuBuilder(new File("path/to/fmu.fmu")
builder.asCoSimulationFmu().newInstance().use { fmu -> 

    //set start values
    
    if (fmu.init()) {
        val dt = 1.0/100
        while (fmu.currentTime) < 10) {
            fmu.doStep(dt);
        }
    }
    
}
```

#### Model Exchange(with integrator) example

##### Java API
```java
FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);
FMUBuilder builder = new FmuBuilder(new File("path/to/fmu.fmu"));
try (FmiSimulation fmu = builder.asModelExchangeFmuWithIntegrator(integrator)
                        .newInstance()) {

    //set start values

    if (fmu.init()) {
       double dt = 1d/100;
       while (fmu.getCurrentTime() < 5) {
           fmu.step(dt);
       } 
    }
    
}
```

##### Kotlin API
```kotlin
val integrator = ClassicalRungeKuttaIntegrator(1E-3)
val builder = FmuBuilder(File("path/to/fmu.fmu"))
builder.asModelExchangeFmuWithIntegrator(integrator)
        .newInstance().use { fmu -> 
        
            //set start values
        
            if (fmu.init()) {
                val dt = 1.0/100;
                while (fmu.currentTime < 5) {
                    fmu.step(dt)
                }
            }

        }
```

## FMU2Jar

Command line tool for packaging an FMU into a Java library. This allows you to use the FMU as any other Java library. 
The generated library also exposes all variables from the FMU through a type safe API.

E.g. an FMU with a variable named "Controller.speed" of type Real, will have the methods

```java
    public double getController_speed();
    public void setController_speed(double speed);
``` 

### Usage

```
usage: fmu2jar
 -fmu <arg>    Path to the FMU
 -help         Prints this message
 -mavenLocal   Should the .jar be published to maven local? (optional)
 -out <arg>    Specify where to copy the generated .jar. Not needed if
               "-mavenLocal true". 

```

##### API example from kotlin
```kotlin
    ControlledTemperature.newInstance().use { fmu ->  
        val value: Double = fmu.parameters.getTemperatureSource_T()   
    } //fmu has been automatically terminated
```
##### API example from java
```java
    try (ControlledTemperature fmu = ControlledTemperature.newInstance()) { 
        double value = fmu.getParameters().getTemperatureSource_T();
    } //fmu has been automatically terminated
```

Here is an example of how the  generated code looks like:

```kotlin

class ControlledTemperature private constructor(
    val fmu: FmiSimulation
) : FmiSimulation by fmu {

    companion object {
        // fmu unpacking code
    }

    val locals = Locals()
    val inputs = Inputs()
    val outputs = Outputs()
    val parameters = Parameters()
    val calculatedParameters = CalculatedParameters()

    inner class Inputs {
        ...
    }

    inner class Outputs {
        
        /**
         * Temperature_Reference
         * Causality=OUTPUT
         * Variability=CONTINUOUS
         */
        fun getTemperature_Reference() = fmu.variableAccessor.getReal(46)
            
        /**
         * Temperature_Room
         * Causality=OUTPUT
         * Variability=CONTINUOUS
         * min=2.0
         * max=4.0
         */
        fun getTemperature_Room() = fmu.variableAccessor.getReal(47)
            
    }
        
    ...
            
}
```

Notice how the javadoc is populated with info from the ```modelDescription.xml```, and variables are sorted by their causality.

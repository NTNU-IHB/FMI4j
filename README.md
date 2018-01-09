# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.mechatronics.sfi.fmi4j/fmi-import)


FMI4j is a software library for dealing with Functional Mock-up Units in Kotlin/Java.
Currently it consists of two parts:

## FMI Import

Allows FMUs for Model Exchange and Co-simulation (version 2.0 only) to be imported in Java applications.
For Model Exchange, solvers are also included


#### Co-simulation example

```java

FmiSimulation fmu = new FmuBuilder(new File("path/to/fmu.fmu"))
                        .asCoSimulationFmu()
                        .newInstance();

//set start values

fmu.init();

double dt = 1d/100;
while (fmu.getCurrentTime() < 10) {
    fmu.doStep(dt);
}

fmu.terminate(); //can also use try with resources

```


#### Model-exchange(with integrator) example

```java

FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);

try (FmiSimulation fmu = new FmuBuilder(new File("path/to/fmu.fmu"))
                        .asModelExchangeFmuWithIntegrator(integrator)
                        .newInstance()) {

    //set start values

    fmu.init();
    
    double dt = 1d/100;
    while (fmu.getCurrentTime() < 5) {
        fmu.step(dt);
    }

}

```

## FMU2Jar

Command line tool for packaging an FMU into a Java library. This allows you to use the FMU as any other Java library. 
The generated library also exposes all variables from the FMU through a type safe API.

E.g. an FMU with a variable named "Controller.speed" of type Real, will have the methods

```java
    public double getController_speed()
    public void setController_speed(double value)
``` 

### Usage

```
usage: java -jar fmu2jar
 -fmu <arg>    Path to the FMU
 -help         Prints this message
 -mavenLocal   Should the .jar be published to maven local? (optional)
 -out <arg>    Specify where to copy the generated .jar. Not needed if
               "-mavenLocal true". 

```

##### API example from kotlin
```kotlin
    ControlledTemperature.newInstance().use{ fmu ->  
        val temperature_Reference: Double = fmu.parameters.getTemperatureSource_T()        
    } //fmu has been automatically terminated
```
##### API example from java
```java
    try (ControlledTemperature fmu = ControlledTemperature.newInstance()) { 
        double temperature_Reference = fmu.getParameters().getTemperatureSource_T()
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

    val inputs = Inputs()
    val outputs = Outputs()
    val parameters = Parameters()
    val calculatedParameters = CalculatedParameters()
    val locals = Locals()

    inner class Inputs {
    }

    inner class Outputs {

        
            /**
             * Causality=OUTPUT
             * Variability=CONTINUOUS
             */
            fun getTemperature_Reference(): Double {
                return fmu.read(46).asReal()
            }
            
            /**
             * Causality=OUTPUT
             * Variability=CONTINUOUS
             */
            fun getTemperature_Room(): Double {
                return fmu.read(47).asReal()
            }
            
            
            ...
            
    }
    
}

```

Notice how the javadoc is populated with info from the ```modelDescription.xml```, and variables are sorted by their causality.

# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/NTNU-IHB/FMI4j/issues)

[![](https://jitpack.io/v/NTNU-IHB/FMI4j.svg)](https://jitpack.io/#NTNU-IHB/FMI4j) 
[ ![Bintray](https://api.bintray.com/packages/ntnu-ihb/mvn/FMI4j/images/download.svg) ](https://bintray.com/ntnu-ihb/mvn/FMI4j/_latestVersion)

[![CircleCI](https://circleci.com/gh/NTNU-IHB/FMI4j/tree/master.svg?style=svg)](https://circleci.com/gh/NTNU-IHB/FMI4j/tree/master)
![](https://github.com/NTNU-IHB/FMI4j/workflows/Build/badge.svg)

[![Gitter](https://badges.gitter.im/NTNU-IHB/FMI4j.svg)](https://gitter.im/NTNU-IHB/FMI4j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


FMI4j is a software package for dealing with Functional Mock-up Units (FMUs) on the Java Virtual Machine (JVM), written in [Kotlin](https://kotlinlang.org/). 

FMI4j supports import of both [FMI](http://fmi-standard.org/) 1.0 and 2.0 for **Co-simulation**. For  **Model Exchange** version 2.0 is supported. <br/>
For Model Exchange, solvers from [Apache Commons Math](http://commons.apache.org/proper/commons-math/userguide/ode.html) can be used.

Export of FMI 2.0 for **Co-simulation** is also supported.

Compared to other FMI libraries targeting the JVM, FMI4j is **considerably faster** due to the fact that we use JNI instead of JNA. 
Considering FMI-import, a significant speedup (2-5x) compared to other open-source FMI implementations for the JVM should be expected. For FMI-export FMI4j is multiple orders of magniutude faster than any open source alternative today.


### <a name="api"></a> FMI import

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

### <a name="api"></a> FMI export

```java
@SlaveInfo(
        modelName = "JavaSlave",
        author = "John Doe"
)
public class JavaSlave extends Fmi2Slave {

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double realOut = 2.0;

    @ScalarVariable(causality = Fmi2Causality.output)
    protected int intOut = 99;

    @ScalarVariable(causality = Fmi2Causality.output)
    protected double[] realsOut = {50.0, 200.0};

    @ScalarVariable(causality = Fmi2Causality.local)
    protected String[] string = {"Hello", "world!"};
    
    @Override
    public boolean doStep(double currentTime, double dt) {
        realOut += dt;
        return true;
    }

}
```

Artifacts are available through [JitPack](https://jitpack.io/#NTNU-IHB/FMI4j) and [Bintray](https://bintray.com/ntnu-ihb/mvn/FMI4j).

___

To get started head over to the [Wiki](https://github.com/NTNU-IHB/FMI4j/wiki)!

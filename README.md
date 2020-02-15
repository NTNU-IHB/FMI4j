# FMI4j #

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/NTNU-IHB/FMI4j/issues)

[![CI](https://github.com/NTNU-IHB/FMI4j/workflows/Build/badge.svg)](https://github.com/NTNU-IHB/FMI4j/actions)
[![Bintray](https://api.bintray.com/packages/ntnu-ihb/mvn/FMI4j/images/download.svg) ](https://bintray.com/ntnu-ihb/mvn/FMI4j/_latestVersion)

[![Gitter](https://badges.gitter.im/NTNU-IHB/FMI4j.svg)](https://gitter.im/NTNU-IHB/FMI4j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


FMI4j is a software package for dealing with Functional Mock-up Units (FMUs) on the Java Virtual Machine (JVM), written in [Kotlin](https://kotlinlang.org/). 

FMI4j supports import of both [FMI](http://fmi-standard.org/) 1.0 and 2.0 for **Co-simulation**. For  **Model Exchange** version 2.0 is supported. <br/>

Export of FMI 2.0 for **Co-simulation** is also supported.

Compared to other FMI libraries targeting the JVM, FMI4j is **considerably faster** due to the fact that we use JNI instead of JNA. 
Considering FMI-import, a significant speedup (2-5x) compared to other open-source FMI implementations for the JVM should be expected. 
For FMI-export FMI4j is multiple orders of magnitude faster than any existing open source alternative.

***

Artifacts are available through [Bintray](https://bintray.com/ntnu-ihb/mvn/FMI4j).


### <a name="api"></a> FMI import

```java

class Demo {
    
    void main(String[] args) {
        
        Fmu fmu = Fmu.from(new File("path/to/fmu.fmu")); //URLs are also supported
        FmuSlave slave = fmu.asCoSimulationFmu().newInstance();

        slave.simpleSetup();
        
        double stop = 10;
        double stepSize = 1.0/100;
        while(slave.getSimulationTime() <= stop) {
            if (!slave.doStep(stepSize)) {
                break;
            }
        }
        slave.terminate(); //or close, try with resources is also supported
        fmu.close();
        
    }
    
}
```

### <a name="api"></a> FMI export

###### Write the code

```java
@SlaveInfo(
        modelName = "MyJavaSlave",
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

    public JaveSlave(String instanceName) {
        super(instanceName);
    }   

    @Override
    public void doStep(double currentTime, double dt) {
        realOut += dt;
    }

}
```
###### Build the FMU

```
Usage: fmu-builder [-h] [-d=<destFile>] -f=<jarFile> -m=<mainClass>
  -d, --dest=<destFile>    Where to save the FMU.
  -f, --file=<jarFile>     Path to the Jar.
  -h, --help               Print this message and quits.
  -m, --main=<mainClass>   Fully qualified name if the main class.
```

*** 

Would you rather build FMUs using Python? Check out [PythonFMU](https://github.com/NTNU-IHB/PythonFMU)! <br>
Or would you rather simulate FMUs using C++? Check out [FMI4cpp](https://github.com/NTNU-IHB/FMI4cpp)! <br>
Need to distribute your FMUs? [FMU-proxy](https://github.com/NTNU-IHB/FMU-proxy) to the rescue! 


# FMI4j

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/NTNU-IHB/FMI4j/issues)

[![CI](https://github.com/NTNU-IHB/FMI4j/workflows/Build/badge.svg)](https://github.com/NTNU-IHB/FMI4j/actions)
[![Gitter](https://badges.gitter.im/NTNU-IHB/FMI4j.svg)](https://gitter.im/NTNU-IHB/FMI4j?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)


FMI4j is a software package for dealing with Functional Mock-up Units (FMUs) on the Java Virtual Machine (JVM), written in [Kotlin](https://kotlinlang.org/). 

FMI4j supports import of both [FMI](http://fmi-standard.org/) 1.0 and 2.0 for **Co-simulation**. For  **Model Exchange** version 2.0 is supported. <br/>

Compared to other FMI libraries targeting the JVM, FMI4j is **considerably faster** due to the fact that we use JNI instead of JNA. 
Considering FMI-import, a significant speedup (2-5x) compared to other open-source FMI implementations for the JVM should be expected. 

Maven artifacts are available through [Central](https://search.maven.org/search?q=g:info.laht.fmi4j)

***

### <a name="api"></a> FMI import

```java

class Demo {
    
    void main(String[] args) {
        
        Fmu fmu = Fmu.from(new File("path/to/fmu.fmu")); //URLs are also supported
        FmuSlave slave = fmu.asCoSimulationFmu().newInstance();

        slave.simpleSetup();
        
        double t = 0;
        double stop = 10;
        double stepSize = 1.0/100;
        while(t <= stop) {
            if (!slave.doStep(t, stepSize)) {
                break;
            }
            t += stepSize;
        }
        slave.terminate(); //or close, try with resources is also supported
        fmu.close();
        
    }
    
}
```

Would you rather build FMUs using a JVM language? Check out [FMU4j](https://github.com/markaren/FMU4j)! <br>
Or maybe build FMUs using Python? Check out [PythonFMU](https://github.com/NTNU-IHB/PythonFMU)! <br>
Or would you rather simulate FMUs using C++? Check out [FMI4cpp](https://github.com/NTNU-IHB/FMI4cpp)! <br>
Need to distribute your FMUs? [FMU-proxy](https://github.com/NTNU-IHB/FMU-proxy) to the rescue! <br>
Need a complete co-simulation framework with SSP support? Check out [Vico](https://github.com/NTNU-IHB/Vico) <br>


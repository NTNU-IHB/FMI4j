# FMI4j #


FMI4j is a software library for dealing with Functional Mock-up Units in Kotlin/Java.
Currently it consists of two parts:

# FMI Import

Allows FMUs for Model Exhange and Co-simulation (version 2.0 only) to be imported in Java applications.
For Model Exchange, solvers are also included


# FMU2Jar

Tool for packaging an FMU into a Java library. This allows you to use the FMU as any other Java library. 


# Co-simulation example

```java

CoSimulationFmu fmu = new CoSimulationFmu(new File("path/to/fmu.fmu"));
fmu.init();

double t = 0;
double dt = 1d/100;

while (t < 10) {
    fmu.doStep(dt);
}

fmu.terminate();

```


# Model-exchange example

```java

FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1E-3);
ModelExchangeFmu fmu = new ModelExchangeFmu(new File("path/to/fmu.fmu"), integrator);
fmu.init();

double microStep = 1E-3;
double macroStep = 1E-2;
while (fmu.getCurrentTime() < 5) {
    fmu.step(microStep, macroStep);
}

fmu.terminate();

```
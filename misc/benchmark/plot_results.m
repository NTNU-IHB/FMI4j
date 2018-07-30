clc, clear;

FMIL = 33;
FMI4j = 370;
JavaFMI = 360;
PyFMI = 586;

c = categorical({'FMIL', 'FMI4j', 'JavaFMI', 'PyFMI'});
y = [FMIL, FMI4j, JavaFMI, PyFMI];

bar(c, y)

ylabel('Time[ms]')
grid on;


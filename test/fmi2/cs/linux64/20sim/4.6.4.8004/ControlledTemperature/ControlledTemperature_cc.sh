#!/bin/bash

cc=./fmuCheck.darwin64
unamestr=`uname`
if [[ "$unamestr" == 'Linux' ]]; then

if $(uname -m | grep '64'); then
  cc=./fmuCheck.linux64
else
  cc=./fmuCheck.linux32
fi

else
   cc=./fmuCheck.darwin64
fi

$cc  -h 1e-4 -l 3 -s 20 -o ControlledTemperature_cc.csv ControlledTemperature.fmu


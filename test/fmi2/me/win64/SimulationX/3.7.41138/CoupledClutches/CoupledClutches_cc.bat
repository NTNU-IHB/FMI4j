@echo off
%~dp0fmuCheck.win64.exe -e "%~dp0CoupledClutches_cc.log" -o "%~dp0CoupledClutches_cc.csv" -i "%~dp0CoupledClutches_in.csv" -l 4 -h 1e-4 -s 1.5 "%~dp0CoupledClutches.fmu
@echo off
%~dp0fmuCheck.win64.exe -e "%~dp0DoublePendulum_cc.log" -o "%~dp0DoublePendulum_cc.csv" -l 4 -h 1e-4 -s 3 "%~dp0DoublePendulum.fmu
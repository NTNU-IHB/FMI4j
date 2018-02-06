set FMUName=inc
fmuCheck.win64.exe -e %FMUName%_cc.log -o %FMUName%_cc.csv -l 6 -h 0.5 -s 15.00 %FMUName%.fmu

set FMUName=dq
fmuCheck.win64.exe -e %FMUName%_cc.log -o %FMUName%_cc.csv -l 6 -h 0.1 -s 1.00 %FMUName%.fmu

set FMUName=bouncingBall
fmuCheck.win64.exe -e %FMUName%_cc.log -o %FMUName%_cc.csv -l 6 -h 0.01 -s 4.00 %FMUName%.fmu

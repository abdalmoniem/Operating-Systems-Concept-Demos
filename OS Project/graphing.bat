echo off
set COMPORT=%1
shift
cd scripts_and_helpers\io_graphing\fast\windows\9600
p_oscilloscope %COMPORT%

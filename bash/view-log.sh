#!/bin/sh

## open logs in text editor

if [ -e '../../../logs/lie.log' ]; then
 'D:\work\npp.6.9.bin\notepad++.exe' ../../../logs/lie.log
else echo "No any logs to view."
fi
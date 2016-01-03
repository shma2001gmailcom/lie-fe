#!/bin/sh

## open logs in text editor

if [ -e '../../../logs/lie.log' ]; then
 gedit ../../../logs/lie.log
else echo "No any logs to view."
fi
#!/bin/sh

## open logs in text editor

if [[ -e '../../../logs/lie.log' ]]; then
 edit ../../../logs/lie.log
else echo "No any logs to view."
fi
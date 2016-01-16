#!/bin/bash

## remove logs

if [ -e '../../../logs/lie.log' ]; then
 rm ../../../logs/lie.log
else echo 'No any logs to remove.'
fi
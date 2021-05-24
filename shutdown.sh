#!/bin/bash
PID=$(ps -ef | grep zuul-0.0.1.jar | grep -v grep | awk '{ print $2 }')
if [ ${PID} ]; 
then
    echo 'BigBee is stpping...'
    echo kill $PID DONE
    kill $PID
else
    echo 'BigBee is already stopped...'
fi

#!/usr/bin/bash
#Clean old namesapces

for ns in `kubectl get ns | grep '^[0-9]' | awk '{print $1}'`
do
    kubectl get all -n $ns | grep pathmind > /dev/null
    if [ $? != 0 ]
    then
        kubectl get all -n $ns | grep selenium > /dev/null
        if [ $? == 0 ]
        then
            kubectl delete ns $ns
        fi
    fi
done

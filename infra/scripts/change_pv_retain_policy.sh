#!/usr/bin/bash

for pv in `kubectl get pv | egrep -v "NAME|Retain" | awk '{print $1}'`
do
	kubectl patch pv "${pv}" -p '{"spec":{"persistentVolumeReclaimPolicy":"Retain"}}'
done

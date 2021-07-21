#!/usr/bin/bash

if [ "$1" == "" ] || [ "$2" == "" ] || [ "$3" == "" ]
then
	echo "Usage $0 <namespace> <environment> <workspace>"
	exit 1
fi

namespace=$1
environment=$2
WORKSPACE=$3
domain="devpathmind.com"
apiuser="api"
apipassword=`kubectl get secret apipassword -o=jsonpath='{.data.APIPASSWORD}' -n ${namespace} | base64 --decode`

#Get the deployment target
canary_weight=`kubectl get configmap canary -n ${namespace} -o jsonpath='{.data.canary_weight}'`
deploy_to=`kubectl get configmap canary -n ${namespace} -o jsonpath='{.data.deploy_to}'`


#Deploy app
helm upgrade --install pathmind${deploy_to} \
	${WORKSPACE}/infra/helm/pathmind \
	-f ${WORKSPACE}/infra/helm/pathmind/values_${environment}${deploy_to}.yaml \
	-n ${namespace}

#send messsage to notify about new version
sleep 30
if [ "${environment}" == "prod" ]
then
	subdomain=""
else
	subdomain="${namespace}."
fi
curl -X POST -u "${apiuser}:${apipassword}" https://${subdomain}${domain}/api/newVersionAvailable

#Update Canary
helm upgrade --install canary \
	${WORKSPACE}/infra/helm/canary \
	-f ${WORKSPACE}/infra/helm/canary/values_${environment}.yaml \
	--set canary_weight=${canary_weight} \
	-n ${namespace}

#Update configmap
if [ "${canary_weight}" != "0" ]
then
	canary_weight=0
	deploy_to=""
else
	canary_weight=100
	deploy_to="-slot"
fi

kubectl create configmap canary \
	--from-literal=canary_weight=${canary_weight} \
	--from-literal=deploy_to=${deploy_to} \
	-n ${namespace} \
	-o yaml \
	--dry-run | kubectl replace -f -

#make sure the libraries for model analyzer are ready
if [ "${environment}" != "staging" ]
then
	curl -X PUT -u "${apiuser}:${apipassword}" https://${subdomain}${domain}/api/MAlib/${environment}
fi

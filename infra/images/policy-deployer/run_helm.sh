helm_name=$1
ENVIRONMENT=$2
JobId=$3
domain_name=$4
NAMESPACE=$5

helm upgrade --install ${helm_name} \
	policy-server/helm/policy-server/ \
	--set image.tag=${ENVIRONMENT}${JobId} \
	--set 'ingress.hosts[0].host'=${ENVIRONMENT}.${domain_name} \
	--set 'ingress.hosts[0].paths[0]'="/${JobId}(/|\$)(.*)" \
	--set 'ingress.tls[0].hosts[0]'=${ENVIRONMENT}.${domain_name} \
	--set 'ingress.tls[0].secretName'=letsencrypt-${ENVIRONMENT} \
	-n ${ENVIRONMENT}

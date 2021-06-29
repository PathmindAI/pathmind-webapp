helm_name=$1
ENVIRONMENT=$2
JobId=$3
domain_name=$4
UrlPath=$5
NAMESPACE=$6

helm upgrade --install ${helm_name} \
	policy-server/helm/policy-server/ \
	-f policy-server/helm/policy-server/values_${ENVIRONMENT}.yaml \
	--set image.tag=${ENVIRONMENT}${JobId} \
	--set 'ingress.hosts[0].host'=api.${ENVIRONMENT}.${domain_name} \
	--set 'ingress.hosts[0].paths[0]'="/policy/${UrlPath}(/|\$)(.*)" \
	--set 'ingress.tls[0].hosts[0]'=api.${ENVIRONMENT}.${domain_name} \
	--set 'ingress.tls[0].secretName'=letsencrypt-${ENVIRONMENT} \
	-n ${ENVIRONMENT}

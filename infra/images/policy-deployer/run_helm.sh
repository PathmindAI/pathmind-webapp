helm_name=$1
ENVIRONMENT=$2
JobId=$3
UrlPath=$4
NAMESPACE=$5

helm upgrade --install ${helm_name} \
	policy-server/helm/policy-server/ \
	-f policy-server/helm/policy-server/values_${ENVIRONMENT}.yaml \
	--set image.tag=${ENVIRONMENT}${JobId} \
	--set 'ingress.hosts[0].paths[0]'="/policy/${UrlPath}(/|\$)(.*)" \
	--set 'ingress.tls[0].secretName'=letsencrypt-${ENVIRONMENT} \
	-n ${ENVIRONMENT}

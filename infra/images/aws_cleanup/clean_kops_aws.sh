#Get the list of used launch templates
USED_LT=`aws autoscaling describe-auto-scaling-groups | grep LaunchTemplateName | cut -f4 -d'"'`

#Check the Launch templates created more than 1 day ago
DATE=`date  --date="1 days ago" "+%Y-%m-%d"`
LT_LIST=`aws ec2 describe-launch-templates \
  | jq -cr ".[] | .[] | select (.CreateTime < \"${DATE}\").LaunchTemplateName" \
  | egrep '^id.*dev-.*|^id.*test-.*|^id.*prod-.*'`

for LT in ${LT_LIST}
do
	#if LT is not use remove it
	echo ${USED_LT} | tr ' ' '\n' | grep "\<${LT}\>" > /dev/null
	if [ $? != 0 ]
	then
		echo "Deleting ${LT}"
		aws ec2 delete-launch-template --launch-template-name ${LT} --region us-east-1
	fi
done

exit 0

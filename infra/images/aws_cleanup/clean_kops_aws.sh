
if [ "$NAME" == "" ] || [ "$BUCKET_NAME" == "" ]
then
	echo "Need to set NAME and BUCKET_NAME env variables"
	exit -1
fi

#set kops context
export KOPS_STATE_STORE="s3://${BUCKET_NAME}/k8s.${NAME}"
kops export kubecfg --name $NAME

#Get the ASG list
ASG_LIST=`aws autoscaling describe-auto-scaling-groups \
	| grep AutoScalingGroupARN \
	| grep AutoScalingGroupARN \
	| cut -f4 -d'"' \
	| grep '/id' \
	| grep $NAME \
	| cut -f2 -d'/'`

#Get running trainigs
JOB_LIST=`kubectl get pods --all-namespaces \
	| grep id \
	| awk '{print $2}'`

#Remove ASG that does not have any training associated
for ASG in ${ASG_LIST}
do
	ID=`echo $ASG | cut -f1 -d'.'`
	echo ${JOB_LIST} | tr ' ' '\n' | cut -f1 -d'-' | grep "\<${ID}\>" > /dev/null
	if [ $? != 0 ]
	then
		echo "Deleting instance group ${ID}"
		kops delete ig ${ID} --yes
		echo "Deleting ASG ${ASG}"
		aws autoscaling delete-auto-scaling-group --auto-scaling-group-name ${ASG} --force-delete
	fi
done
#Get the list of used launch templates
USED_LT=`aws autoscaling describe-auto-scaling-groups | grep LaunchTemplateName | cut -f4 -d'"'`

#Check the Launch templates created more than 1 day ago
DATE=`date  --date="2 hours ago" "+%Y-%m-%d %H:%M:%S"`
LT_LIST=`aws ec2 describe-launch-templates \
  | jq -cr ".[] | .[] | select (.CreateTime < \"${DATE}\").LaunchTemplateName" \
  | egrep "^id.*${NAME}.*"`

for LT in ${LT_LIST}
do
	#if LT is not use remove it
	echo ${USED_LT} | tr ' ' '\n' | grep "\<${LT}\>" > /dev/null
	if [ $? != 0 ]
	then
		echo "Deleting launch configuration ${LT}"
		aws ec2 delete-launch-template --launch-template-name ${LT} --region us-east-1
	fi
done

exit 0

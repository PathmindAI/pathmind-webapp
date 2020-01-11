#!/bin/bash

sleep_time=60
s3_url="s3://${S3BUCKET}/${S3PATH}"
aws s3 sync ${s3_url} ./

bash script.sh &

sleep $sleep_time

while true
do
        ps -ef | grep -v grep | grep 'script.sh' > /dev/null
        if [ $? != 0 ]
        then
                break
        fi
        #Upload files
        aws s3 cp --recursive ./work/PPO ${s3_url}/output/
        aws s3 cp ./work/trial_complete ${s3_url}/output/trial_complete
        aws s3 cp ./work/trial_error ${s3_url}/output/trial_error
        aws s3 cp ./work/trial_list ${s3_url}/output/trial_list
        sleep $sleep_time
done

#Generate final files
mkdir -p result
for DIR in `find "./work/" -iname model -type d`; do
        cd $DIR
        mkdir -p $OLDPWD/../result/$(basename `dirname $DIR`)/
        cp ../progress.csv $OLDPWD/../result/$(basename `dirname $DIR`)
        cp ../../*.json $OLDPWD/../result/
        zip -r $OLDPWD/../result/policy_$(basename `dirname $DIR`).zip .
        aws s3 cp $OLDPWD/../result/policy_$(basename `dirname $DIR`).zip ${s3_url}/output/
        cd $OLDPWD
        cp trial_* ../result
        cd `fin d "$DIR"/.. -iname checkpoint_* -type d | sort -V | tail -1`
        zip $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip ./*
        aws s3 cp $OLDPWD/../result/$(basename `dirname $DIR`)/checkpoint.zip ${s3_url}/output/
        cd $OLDPWD
done


START=1
SLEEP_TIME=10
LOG_DIR="/home/LogFiles/"
RESTART_LOG_FILE=${LOG_DIR}"/log_${COMPUTERNAME}.txt"
#Send a message to slack notifying that the VM is started
MESSAGE='{"text":"'${APPLICATION_URL}' VM '${COMPUTERNAME}' Started"}'
echo $MESSAGE > /tmp/msg.json
wget -O- --post-file=/tmp/msg.json --header='Content-Type:application/json'  $SLACK_WEBHOOK_URL
while true
do
    DATE=`date +"%Y_%m_%d"`
    LOG_FILE_MASK=${LOG_DIR}"/"${DATE}"*"${COMPUTERNAME}"*docker.log"
    RESTART_HISTORY_FILE=${LOG_DIR}"/"${DATE}"_"${COMPUTERNAME}"_restart.log"
    if echo $START | grep 1 > /dev/null
    then
        #first time the script runs, update the restart history control file
        grep -h OutOfMemoryError ${LOG_FILE_MASK} | awk '{print $1}' | sort -u > $RESTART_HISTORY_FILE
        START=0
        continue 1
    fi
    ERROR_LIST=`grep -h OutOfMemoryError ${LOG_FILE_MASK} | awk '{print $1}' | sort -u`
    for ERROR in $ERROR_LIST
    do
        #Check if there was a restart for this error ocurrence
        grep $ERROR $RESTART_HISTORY_FILE > /dev/null
        if ! grep $ERROR $RESTART_HISTORY_FILE > /dev/null
        then
            #New error was found
            echo `date`" ${COMPUTERNAME} restart started" >> $RESTART_LOG_FILE
            PID=`ps -ef | grep java | egrep -v "container|grep" | awk '{print $1}'`
            echo `date`" ${COMPUTERNAME} java pid=${PID}" >> $RESTART_LOG_FILE
            #UPdate restart history control file
            grep -h OutOfMemoryError ${LOG_FILE_MASK} | awk '{print $1}' | sort -u > $RESTART_HISTORY_FILE
            #Send Notification to slack
            MESSAGE='{"text":"Out of memory found in '${APPLICATION_URL}' VM '${COMPUTERNAME}' restarting instance"}'
            echo $MESSAGE > /tmp/msg.json
            wget -O- --post-file=/tmp/msg.json --header='Content-Type:application/json'  $SLACK_WEBHOOK_URL
            #Restart instance
            kill -9 ${PID}
            break
        fi
    done
    sleep $SLEEP_TIME
done

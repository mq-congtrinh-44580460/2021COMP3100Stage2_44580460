#!/bin/bash
# have your client and ds-server in the same directory 
# to kill multiple runaway processes, use 'pkill runaway_process_name'
diffLog="stage1.diff"
passed=0
total=0
if [ $# -lt 2 ]; then
    echo "Usage: -c configDir"
    exit
else
    javac DSClient.java parser/JobSpec.java parser/Parser.java parser/ServerSpec.java scheduler/Scheduler.java
fi

while getopts "c:" opt;
do
    echo "$opt $OPTARG"
    case ${opt} in
        c)  configDir=$OPTARG;;
    esac
done

if [ -f $configDir/$diffLog ]; then
	rm $configDir/*.log
	rm $configDir/$diffLog
fi

if [ ! -d $configDir ]; then
	echo "No $configDir found!"
	exit
fi

trap "kill 0" EXIT

for conf in $configDir/*.xml; do
	((total++))
	echo "$conf"
	echo "running the reference implementation (./ds-client)..."
	sleep 1
	if [ -z $1 ]; then
		./ds-server -c $conf -v brief > $conf.ref.log&
		sleep 1
		./ds-client
	else
		./ds-server -c $conf -v brief -n > $conf.ref.log&
		sleep 1
		./ds-client -n
	fi

	echo "running your implementation (DSClient) in Java..."
	sleep 2
	if [ -z $1 ]; then
		./ds-server -c $conf -v brief > $conf.your.log&
	else
		./ds-server -c $conf -v brief -n > $conf.your.log&
	fi
	sleep 1	
    java DSClient
	sleep 1
	diff $conf.ref.log $conf.your.log > $configDir/temp.log
	if [ -s $configDir/temp.log ]; then
		echo NOT PASSED!
	else
		echo PASSED!
		((passed++))
	fi
	echo ============
	sleep 1
	cat $configDir/temp.log >> $configDir/$diffLog
done

if [ -f DSClient.class ]; then
	rm DSClient.class
	rm parser/JobSpec.class
	rm parser/Parser.class
    rm parser/ServerSpec.class
    rm scheduler/Scheduler.class
fi

if [ -f ds-system.xml ]; then
	rm ds-system.xml
fi

if [ -f ds-jobs.xml ]; then
	rm ds-jobs.xml
fi

echo "$passed/$total tests passed! check $configDir/$diffLog"
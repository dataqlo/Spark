#!/bin/bash
# Author: github.com/AbhishekSolanki

receiveData=true

while $receiveData
do
 payload=$(curl -s -get http://localhost:8080/BOD/rest/Transaction/data/)
 # exit condition when all the data is read
 echo $payload | grep -q "HTTP Status 500"
 if [ $? -eq 0 ]; then echo "end"; receiveData=false; break; fi
 cardType="${payload:0:1}"
 if [ $cardType == "<" ];then
 	echo "kafka_mastercard_q: "$payload
 elif [ $cardType == "[" ]; then
 	echo "kafka_visa_q: "$payload
 else
 	echo "kafka_amex_q: "$payload
 fi
sleep 0.25
done

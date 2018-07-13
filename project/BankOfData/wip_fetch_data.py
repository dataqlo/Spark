#! /usr/local/bin/python2.7
# Author: github.com/AbhishekSolanki

import urllib2
import time
# install kafka library for python - https://github.com/dpkp/kafka-python
# sudo pip2.7 install kafka-python
from kafka import KafkaProducer

# kafka producer connects to server
producer = KafkaProducer(bootstrap_servers='localhost:9092')

receiveData = True
while receiveData:
 response = urllib2.urlopen('http://localhost:8080/BOD/rest/Transaction/data/')
 payload = response.read()
 if "HTTP Status 500" in payload:
 	receiveData = False
 	break
 cardType = payload[:1]
 if cardType == "<":
 	producer.send('bod-q-mastercard', payload)
 	print str(time.time())+" payload pushed to bod-q-mastercard !"
 elif cardType == "[":
 	producer.send('bod-q-visa', payload)
 	print str(time.time())+" payload pushed to bod-q-visa !"
 else:
   producer.send('bod-q-amex', payload)
   print str(time.time())+" payload pushed to bod-q-amex !"
 time.sleep(1)



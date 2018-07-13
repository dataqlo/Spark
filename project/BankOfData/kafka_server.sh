# kills the kafka server if already running 
kill -9 `cat logs/kafka_server.pid`
# starting kafka server
echo "`date` INFO: Starting kafka server"
nohup sh /usr/lib/kafka/bin/kafka-server-start.sh /usr/lib/kafka/config/server.properties --override delete.topic.enable=true > logs/kafka_server.out 2>&1 &
echo $! > logs/kafka_server.pid
echo "`date` INFO: Kafka server started !"

# delete / create kafka topics for BOD
echo "`date` INFO: Creating Kafka topics"
#kafka-topics --zookeeper localhost:2181 --delete --topic bod-q-mastercard
#kafka-topics --zookeeper localhost:2181 --delete --topic bod-q-visa
#kafka-topics --zookeeper localhost:2181 --delete --topic bod-q-amex
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic bod-q-mastercard
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic bod-q-visa
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic bod-q-amex
echo "`date` INFO: Kafka Topics Created !"
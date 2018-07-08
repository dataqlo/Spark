// Kafka

// Kafka runs on multiple cluster and can span across multiple datacenteres 
// Topics: stream of records stored in category on kafka cluster
// Eack record is stored in KV pair and a timestamp
// Producers: allows application to publish stream of records to topics
// Consumer: allows application to subscribe to one or more topics 
// Stream: allows application to act as producer and Consumer, effectively transformation input stream to output stream
// Connector: eg. relationl db Connector, captures changes to table and publish to a topics


// Topics 
// cataegory of feed name to which records will be published.
// topics are multi-subscribers
// there is a partition log for each topic on cluster
// each record is assigned a sequence id called offset, which uniquely identifies record in partition
// retention period can be configured, for data to be available in topic
// the offset is controlled by consumer


// start the server 
// sh /usr/lib/kafka/bin/kafka-server-start.sh /usr/lib/kafka/config/server.properties
//  --override delete.topic.enable=true \
//  --override broker.id=100 \

// create topic 
// kafka-topics --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic topic_name_1

// list available topics
// kafka-topics --zookeeper localhost:2181 --list

// describing topic
// kafka-topics --zookeeper localhost:2181 --describe --topic topic_name_1

// delete topic (  --override delete.topic.enable=true - should be used while starting the kafka server to delete topic )
// kafka-topics --zookeeper localhost:2181 --delete --topic topic_name_1
// Spark Sreamming
// Spark Streaming provides high level abstraction called as discretized Stream aka DStream, which represents continuous stream of data
// DStreams are sequence of RDDs 
// StreamingContext is the main entry point for all streaming application. Which takes no. of threads and batch interval configuration

import org.apache.spark._
import org.apache.spark.streaming._
val ssc = new org.apache.spark.streaming.StreamingContext(sc, Seconds(1)) 
//ssc: org.apache.spark.streaming.StreamingContext = org.apache.spark.streaming.StreamingContext@6a7c0932

// For scala app:
val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
val ssc = new StreamingContext(conf, Seconds(1))

// create a stream of source from a tcp source via nc -lk 9999 
// word1
// word1
// word2
// ctrl+d

// create a DStream that will connect to a tcp source
val lines = ssc.socketTextStream("localhost", 9999)
//lines: org.apache.spark.streaming.dstream.ReceiverInputDStream[String] = org.apache.spark.streaming.dstream.SocketInputDStream@3fd64d6b
val words = lines.flatMap(_.split(" "))
val pairs = words.map(word => (word, 1))
val wordCounts = pairs.reduceByKey(_ + _)
wordCounts.print()

// to start the processing after all the transformation calll
ssc.start()             // Start the computation
ssc.awaitTermination()  // Wait for the computation to terminate

// points to remember
// 1. Once the streaming context has started, no new streaming computation can be added or removed
// 2. Once the context has been stoped it can not be restared
// 3. Only one Streaming context can be active in JVM
// 4. stop() on Streaming Context also stops SparkContext. This is handled by adding optional parameter in stop()
// stopSparkContext to false
// 5. A SparkContext can be re-used to create multiple StreamingContexts, as long as the previous StreamingContext is stopped 
// (without stopping the SparkContext) before the next StreamingContext is created.
// 5. While running streaming locally do not use local, local[1] as master URL, this means only one theread will receives data and
// no thread for processing the received data. Set min theread to 2
// 6. The no. of core for spark streaming should be more than no. of receivers, else the system will receive data but not be able to process


// DStream
// DStream contains data from a certain interval
// Any operation on DStream translates to the underlying RDDs
// Input DStream: stream of data received from streaming source except file stream is associated with a "Receiver" Object
// 2 Category of built in source - BASIC: File System, Socket Connection ADVANCE: Kafka, Flume, Twitter

// Kafka Integration ( read day14.scala for basic understanding of kafka )


// write some data to topic: topic-name-1
// kafka-console-producer --broker-list localhost:9092 -opic topic-name-1

//Approach-1 Receiver based
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

val ssc = new StreamingContext(sc, Seconds(2))
val kafkaStream = KafkaUtils.createStream(ssc, "localhost:2181","spark-streaming-consumer-group", Map("topic-name-1" -> 5))
kafkaStream.print()
ssc.start()
ssc.awaitTermination()


// Approach-2 Direct ( no receiver ) FOR KAFKA BELOW 0.10
// spark-shell --jars /usr/lib/kafka/libs/kafka_2.11-0.10.2-kafka-2.2.0.jar
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import  org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import kafka.serializer.StringDecoder // this will not work so need to use the below import statement
import _root_.kafka.serializer.StringDecoder


val ssc = new StreamingContext(sc, Seconds(5))
val topicsSet = Set("topic-name-1")


val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "groupId"
 )

 val messages= KafkaUtils.createDirectStream[String, String,StringDecoder, StringDecoder](
  ssc, kafkaParams, topicsSet)


messages.print()
ssc.start()
ssc.awaitTermination()

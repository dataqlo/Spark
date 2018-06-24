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



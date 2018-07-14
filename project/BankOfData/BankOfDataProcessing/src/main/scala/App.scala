/*
 * Author: https://github.com/AbhishekSolanki
 * Date: 14th July 2018
 * Description: Main file for the application which connects to kafka topics and receives data
*/
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.kafka.clients.consumer.ConsumerConfig
import _root_.kafka.serializer.StringDecoder


object App {
	def main(args: Array[String]): Unit = {
	  // create spark configuration with 2 threads and app name to be visible in yarn
	  val conf = new SparkConf().setMaster("local[2]").setAppName("BOD_kafka_process")
	  // create spark streaming context with batch interval of 10 seconds
		val ssc = new StreamingContext(conf, Seconds(10))

	  // set of topics to which consumer will be subscribed to 
		val topicSet = Set("bod-q-amex","bod-q-mastercard","bod-q-visa")
		// default kafka parameters which specifies server details
		val kafkaParams = Map[String, String](
		    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
				ConsumerConfig.GROUP_ID_CONFIG -> "100"
		    )

		// data received from all the topics in form of (null,data)
		val payLoad= KafkaUtils.createDirectStream[String, String,StringDecoder,StringDecoder](
		    ssc, kafkaParams, topicSet)
		// creating object of Algorithm class which contains logic, class should be serializables
		val algoObj = new Algorithms()
	  // prints the first 10 records, just for dev testing purpose
	  payLoad.print()
	  // for each record in topic we apply map function to parse the receive data using dataProcess method
		payLoad.map(dataPayLoad=>algoObj.dataProcess(dataPayLoad._2)).print
		// start the computation
		ssc.start()
		// waiting for computation to terminate
		ssc.awaitTermination()
	}
}
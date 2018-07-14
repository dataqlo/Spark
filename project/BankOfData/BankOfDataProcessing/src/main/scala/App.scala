import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.kafka.clients.consumer.ConsumerConfig
import _root_.kafka.serializer.StringDecoder


object App {
	def main(args: Array[String]): Unit = {
			val conf = new SparkConf().setMaster("local[2]").setAppName("BOD_kafka_process")
					val ssc = new StreamingContext(conf, Seconds(10))

					val topicSet = Set("bod-q-amex","bod-q-mastercard","bod-q-visa")
					val kafkaParams = Map[String, String](
							ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
							ConsumerConfig.GROUP_ID_CONFIG -> "100"
							)

					val payLoad= KafkaUtils.createDirectStream[String, String,StringDecoder,StringDecoder](
							ssc, kafkaParams, topicSet)

					val algoObj = new Algorithms()

					payLoad.print()
					payLoad.map(dataPayLoad=>algoObj.dataProcess(dataPayLoad._2)).print

					ssc.start()
					ssc.awaitTermination()

	}
}
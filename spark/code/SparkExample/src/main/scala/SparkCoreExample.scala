
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SparkCoreExample {
   def main(args: Array[String]): Unit = {
     // create spark context by creating new SparkContext object and pass SparkConfig object in constructor
    val sc = new SparkContext(
        new SparkConf().setAppName("SparkCoreExample").setMaster("local[2]")
        )
    // create RDD by specifying the complete HDFS path
    val fileRDD = sc.textFile("hdfs://localhost:8020/user/cloudera/dataset/retail_db/orders/part-00000")
    
    // Perform filter, transformation and actions
    val julyCompleteOrderWithSum = fileRDD.filter( x=> {
	    val splitedData = x.split(",")
	    if(splitedData(1).contains("2013-07") && splitedData(3)=="COMPLETE") true else false
     }).map(x => x.split(",")(2).toFloat).sum

// Display result     
println("Revenue of 'COMPLETED' orders for July: "+julyCompleteOrderWithSum)
  }
}
// scala io, spark io
// array, list, tuple, set
// scala data to spark fileRDD
// filter, map, reduce, sum
// accessing list  using ._ notation
// view functions and datatypes of rdd to use correct form
// take, collect, foreach, println
// Difference between scala map function and spark map function when to apply


// Read file from scala, convert local scala variable to spark's rdd
import scala.io.Source
val file = Source.fromFile("/home/cloudera/Desktop/dataset/retail_db/orders/part-00000").getLines()
val fileSeq = file.toList
val fileRDD = sc.parallelize(fileSeq)

// Read file using spark context
val fileRDD = sc.textFile("dataset/retail_db/orders/part-00000")

// simple spark map yeids 1st colum of csv
fileRDD.map( x=> x.split(",")(0))


// map function without using shortcodes
val orderIdAndDate = fileRDD.map( x=> {
	val splitedData = x.split(",")
	splitedData(0), splitedData(1)
})


// sum, filter without using shortcodes
val julyCompleteOrderWithSum = fileRDD.filter( x=> {
	val splitedData = x.split(",")
	if(splitedData(1).contains("2013-07") && splitedData(3)=="COMPLETE") true else false
}).map(x => x.split(",")(2).toFloat).sum


// sum using reduce and filter with shortcodes
val julyCompleteOrderWithReduce = fileRDD.filter( x => x.split(",")(1).contains("2013-07") && x.split(",")(3)=="COMPLETE")).
map(x => x.split(",")(2).toFloat).reduce( (acc,total) = acc+total )
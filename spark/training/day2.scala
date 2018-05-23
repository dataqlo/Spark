// first,groupByKey, reduceByKey, sortByKey, mapPartitionsWithIndex
// join, leftOuterJoin, rightOuterJoin
// Scala Options i.e some or none
// compactBuffer in spark 

val orders = sc.textFile("/user/cloudera/dataset/retail_db/orders")
val orderItems = sc.textFile("/user/cloudera/dataset/retail_db/order_items")

//check the first record
orders.first()

val ordersMap = orders.map( order => (order.split(",")(0).toInt,order.split(",")(1).substring(0,10)))
val orderItemsMap = orderItems.map( orderitem => {
	val oi = orderitem.split(",")
	( oi(1).toInt, oi(4).toFloat)
})

// Revenue sorted by key in ascending order
orderItemsMap.reduceByKey((total,revenue)=> total+revenue).sortByKey().saveAsTextFile("dataset/xml/aaa1.txt")

// Revenue sorted by key in descending order
orderItemsMap.reduceByKey((total,revenue)=> total+revenue).sortByKey(false).take(10).foreach(println)



// inner join
val ordersJoin = ordersMap.join(orderItemsMap)
// leftOuterJoin
val ordersLeftOuterJoin = ordersMap.leftOuterJoin(orderItemsMap)
// some or none if unsure if data exists 
val ordersLeftOuterJoinFilter = ordersLeftOuterJoin.filter( order => order._2._2 == None)
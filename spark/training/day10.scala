//Dataframe
// Is a distributed collection of data organized into named columns. Conceptually equivalent to relational tables
// can be constructed from files, hive tables, any external databases or existing RDDs

//entry point for sparksql is SqlContext class whic requires existing spark context
val sc: SparkContext // existing spark context
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
// to implicitely convert rdd into datframes 
import sqlContext.implicits._

// HiveContext provides superset of SqlContext additional features includes HiveQL Parser, UDFs.
// to select between parser engine use spark.sql.dialect with values hiveql or sql another way is SqlContext.setconf

val sqlContext = new org.apache.spark.sql.SQLContext(sc)
//sqlContext: org.apache.spark.sql.SQLContext = org.apache.spark.sql.SQLContext@6c3e2c3d
import sqlContext.implicits._
//import sqlContext.implicits._

// Defining the named columns structure and its datatypes using case classes
case class Orders(order_id: Int, order_date: String, order_customer_id: Int, order_status: String)
//defined class Orders

// Create object of class for each record and convert to dataframe 
val ordersData = sc.textFile("/user/cloudera/dataset/retail_db/orders/part-00000").map( x => {
val rec = x.split(",")
Orders(rec(0).toInt, rec(1), rec(2).toInt, rec(3))
}).toDF()
//ordersData: org.apache.spark.sql.DataFrame = [order_id: int, order_date: string, order_customer_id: int, order_status: string]

// data needs to be mapped to a table name with registerTempTable function
ordersData.registerTempTable("ordersTable")

// using the table name mentioned in the registerTempTable perform queries
val ordersCount =sqlContext.sql("select count(*) from ordersTable")
//ordersCount: org.apache.spark.sql.DataFrame = [_c0: bigint]

ordersCount.show
//+-----+
//|  _c0|
//+-----+
//|68883|
//+-----+

//operations
// df.show : shows the content of the dataframe
// df.printschema : print the schema in tree format 
// df.select("column_name") : select particular columns
// df.select("some_column" > 1 ) OR select("some_column" +1 ) : to comapre or to increment values
// df.filter("some_column" > 10 ) : filter out records from a particular column based on some condition
// df.groupBy("column_name") : group the data based on some column_name
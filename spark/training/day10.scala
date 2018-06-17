//Dataframe
// Is a distributed collection of data organized into named columns. Conceptually equivalent to relational tables
// can be constructed from files, hive tables, any external databases or existing RDDs
// can be operated as normal RDDs

//entry point for sparksql is SqlContext class whic requires existing spark context
val sc: SparkContext // existing spark context
val sqlContext = new org.apache.spark.sql.SQLContext(sc)
// to implicitely convert rdd into datframes 
import sqlContext.implicits._

// HiveContext provides superset of SqlContext additional features includes HiveQL Parser, UDFs.
// to select between parser engine use spark.sql.dialect with values hiveql or sql another way is SqlContext.setconf


// SparkSql supports two different methods of converting RDDs to Dataframe
// Method 1 "Reflection" : Infer the schema of an RDD that contains specific type of objects. Leads to more consise code and should be
// used when schema is already known while writing application. (line 20-48)
// Method 2 "Programmatic Interface" : First create a schema and apply to existing RDD (line)

// Relections
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




//Programmatic schema
// 1. Create rdd of Rows from original rdd
// 2. Create schema represented by StructType matching the structure of Rows in the step 1
// 3. Apply the schema to RDD by createDataFrame

val sqlContext = new org.apache.spark.sql.SQLContext(sc)
val ordersData = sc.textFile("/user/cloudera/dataset/retail_db/orders/part-00000")
val schemaString = "order_id order_date order_customer_id order_status"
// Import Row.
import org.apache.spark.sql.Row;

// Import Spark SQL data types
import org.apache.spark.sql.types.{StructType,StructField,StringType};

val schema = StructType(
schemaString.split(" ").map(
 fieldName => StructField(fieldName,StringType,true)
 )
)

// convert records from file into Rows
val ordersRDD = ordersData.map(_.split(",")).map(rec=>Row(rec(0),rec(1),rec(2),rec(3)))
val ordersRDDFrame = sqlContext.createDataFrame(ordersRDD,schema)
ordersRDDFrame.registerTempTable("ordersTable")
val res = sqlContext.sql("select count(*) from ordersTable")
res.foreach(println)
//[68883]


//operations
// df.show : shows the content of the dataframe
// df.printschema : print the schema in tree format 
// df.select("column_name") : select particular columns
// df.select("some_column" > 1 ) OR select("some_column" +1 ) : to comapre or to increment values
// df.filter("some_column" > 10 ) : filter out records from a particular column based on some condition
// df.groupBy("column_name") : group the data based on some column_name



// Datasets
// Similar to RDD, but instead of using java searialization / kyro it uses special encoder to serialize data over 
// network and, but dataset allows spark to perform filtering, sorting and hashing without deserialiing the bytes into object.
case class Orders(order_id: Int, order_date: String, order_customer_id: Int, order_status: String)
val ordersData = sc.textFile("/user/cloudera/dataset/retail_db/orders/part-00000").map( x => {
val rec = x.split(",")
Orders(rec(0).toInt, rec(1), rec(2).toInt, rec(3))
}).toDS()
// ordersData: org.apache.spark.sql.Dataset[Orders] = [order_id: int, order_date: string, order_customer_id: int, order_status: string]

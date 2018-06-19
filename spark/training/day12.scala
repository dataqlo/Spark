// File Formats

// 1. AVRO
// The file is defined by avro schema; while searializing / deserializing data, schema is used. Schema is sent with data or is stored 
// within the data. Avro data + schema is fully self describing format. Data and schema are represented in JSON
// schema contains: namespace, type ( "record" by default), fields[], doc for commenting
// fields[].name and fields[].type for each column


// create an avro file with schem and data
// member schema: https://github.com/AbhishekSolanki/Spark/tree/master/dataset/avro/member.avsc
// member data: https://github.com/AbhishekSolanki/Spark/tree/master/dataset/avro/member.json

// creating avro file by avro-tools
//avro-tools fromjson --schema-file member.avsc member.json > member.avro
// hadoop fs -put member.avro dataset/avro/


val sqlContext = new org.apache.spark.sql.SQLContext(sc)
// import needed for the .avro method to be added
import com.databricks.spark.avro._

val df = sqlContext.read.
 format("com.databricks.spark.avro").
 load("dataset/avro/member.avro")

//df: org.apache.spark.sql.DataFrame = [id: string, name: string, timestamp: bigint]

val df1 = sqlContext.read.avro("dataset/avro/member.avro")
//df1: org.apache.spark.sql.DataFrame = [id: string, name: string, timestamp: bigint]

//scala> df1.show
//+----+------------+----------+
//|  id|        name| timestamp|
//+----+------------+----------+
//|1001|    John Doe|1366123856|
//|1002|    Jane Doe|1366489544|
//|1002|Jason Bourne|1366489549|
//+----+------------+----------+
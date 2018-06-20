// File Formats
// AVRO line (4-80)

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

// the databricks library can be specified externally by using --package option in spark-shell or spark-submit
// bin/spark-shell --packages com.databricks:spark-avro_2.11:4.0.0


val sqlContext = new org.apache.spark.sql.SQLContext(sc)

val df = sqlContext.read.
 format("com.databricks.spark.avro").
 load("dataset/avro/member.avro")

//df: org.apache.spark.sql.DataFrame = [id: string, name: string, timestamp: bigint]

// import needed for the .avro method to be added
import com.databricks.spark.avro._
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

// Specifying avero schema
import org.apache.avro.Schema
import java.io._
val schema = new Schema.Parser().parse(new File("/home/cloudera/Desktop/dataset/avro/member.avsc"))
val df = sqlContext.read.
 format("com.databricks.spark.avro").
 option("avroSchema",schema.toString).
 load("hdfs://localhost:8020/user/cloudera/dataset/avro/member.avro")
df.show

// Compression Options
// 3 types of compression : uncompressed, snappy and deflate ( in deflate you can specify the level)
import com.databricks.spark.avro._

sqlContext.setConf("spark.sql.avro.compression.codec","deflate") // Program: sqlContext.conf.set("spark.sql.avro.compression.codec","deflate")
sqlContext.setConf("spark.sql.avro.deflate.level","5") // Program: sqlContext.conf.set("spark.sql.avro.deflate.level","5")
val df = sqlContext.read.avro("dataset/avro/member.avro")
df.write.avro("dataset/avro/member_compressed_deflate.avro")
//rw-r--r--   1 cloudera cloudera          0 2018-06-19 18:45 hdfs://localhost:8020/user/cloudera/dataset/avro/member_compressed_deflate.avro/_SUCCESS
//-rw-r--r--   1 cloudera cloudera        307 2018-06-19 18:45 hdfs://localhost:8020/user/cloudera/dataset/avro/member_compressed_deflate.avro/part-r-00000-8be18a9b-17df-48c5-9ae9

// Avro write partition 
df.write.partitionBy("id").avro("dataset/avro/member_partition/")

//-rw-r--r--   1 cloudera cloudera          0 2018-06-19 18:51 hdfs://localhost:8020/user/cloudera/dataset/avro/member_partition/_SUCCESS
//drwxr-xr-x   - cloudera cloudera          0 2018-06-19 18:51 hdfs://localhost:8020/user/cloudera/dataset/avro/member_partition/id=1001
//drwxr-xr-x   - cloudera cloudera          0 2018-06-19 18:51 hdfs://localhost:8020/user/cloudera/dataset/avro/member_partition/id=1002


// Specifying avro name and namespace via parameter
val name = "AvroParameterTest"
val namespace = "com.dataqlo.spark.avro"
val parameters = Map("recordName" -> name, "recordNamespace" -> namespace)

df.write.options(parameters).avro("dataset/avro/member_param_test")
// the output file will now have new name and namespace values
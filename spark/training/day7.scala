// aggregateByKey an efficient alternative for groupByKey()
// In groupByKey() all the KV pairs is shuffeled across network to a single reducer.

// Find out the distinct key from the given array of KV paris.

val KVRaw = Array( ("a",1),("a",2),("a",3),("b",1),("b",2),("c",1) )
val KV = sc.parallelize(KVRaw)

val distinctKeys = KV.groupByKey().distinct() // we'll implement this same in aggregatebyKey

// aggregatebyKey(1)(2.1,2.2) takes 2 arguments, 1 is initialize value, 2.1 is combiner function and 2.2 is merge function
// 1. initialize value that will not affect the final collected value  e.g 0, (), emnpty list,set etc
// 2.1. combiner function that will work on each partition with initalize value and compute the result 
//      on each node. It takes 2 parameters, 2nd parameter is merged into first parameter
// 2.2 merger function : takes 2 parameter the input type is same as combiner output type and it'll merge the
//     all partitions and generate final output. 


//1
import scala.collection.mutable.HashSet
val partionHashSet = HashSet.empty[String]

//2.1
// the map function gives (K,V) for each K, hence eachRec is in form of (String,Int)
val combinerFunction = (partionHashSet:HashSet[String],eachRec:(String,Int)) =>{

val (stringVal: String, value:Int) = eachRec

// Adding key values on each partition to empty HashSet, += is appending the value to a set
partionHashSet += stringVal

}

//2.2
// The result of all the combiner across paritions is added over here, two sets are merged with ++=
val mergeFunction  = (partionHashSet1:HashSet[String],partionHashSet2:HashSet[String]) =>{
	partionHashSet1++=partionHashSet2
}

// Applying aggregatebyKey on KV
val distinctKeys = KV.map(x=>(x._1,x)).aggregateByKey(partionHashSet)(combinerFunction,mergeFunction)




// combineByKey - alternative to groupByKey 
val KVRaw = Array( ("a",1),("a",2),("a",3),("b",1),("b",2),("c",1) )
val KV = sc.parallelize(KVRaw)

type occuranceOfNumber = (Int, Int)

// combineByKey takes the first argument as function insted of values as in aggregarByKey
val createCombiner = ( num: Int)  =>  (1, num) : occuranceOfNumber

// combiner will append the output to createCombiner with each Value
val combiner = ( acc: occuranceOfNumber, rec: Int ) => {
	( acc._1 + 1 , acc._2 + rec )
}

// the value of combiner from each partition is merged into one
val merger = ( partition1: occuranceOfNumber, partition2: occuranceOfNumber ) => {
	( partition1._1 + partition2._2, partition1._2 + partition2._2 )
}

// using combineByKey
// K is passed to createCombiner for e.g (a,1) 1 is passed to createCombiner and return (1,1)
// the value of combiner is passed to combiner with V to merge it into new / existing K on the given partition.
// merger will merge the computed value by combiner from all partition
val  countNumberAndTotal = KV.combineByKey(createCombiner,combiner,merger)

// declaring type of average (K,no. of occurance, total value)
type forAverageFunction = (String,(Int,Int))

// implementing averaging function
val averageFunction = ( pairRDD: forAverageFunction)  => {
	val num  =  pairRDD._2._1
	val total = pairRDD._2._2
	(pairRDD._1, total/num)
}

// calculating averages
val average = countNumberAndTotal.collectAsMap().map(averageFunction)
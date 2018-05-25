// Scala: fold, foldLeft, foldRight, foldByKey


// fold() 
// fold(initial_value){ (accumulator,each_value_in_list) => some_operation } 
// The initial_value is applied to the each parition once some operation is performed as well as well as when the 
// result of each parition is added 
// It does not take particular order for list value during computation
val numberListRDD = sc.parallelize( ( 1 to 3 ) toList )
numberListRDD.partitions.length 
//res9: Int = 1
val sumUsingFold = numberListRDD.fold(0){ (acc,i) =>
 acc+i
}
//sumUsingFold: Int = 6 
// there is only one parition so (1+2+3+0) +0 after combining each partition result
val sumUsingFold = numberListRDD.fold(1){ (acc,i) =>
 acc+i
}
//sumUsingFold: Int = 8
//there is only one parition so (1+2+3+1) +1 after combining each partition result

//foldLeft() and foldRight()
//Similar to fold(), but left iterates from L to R and right iterates from R to L for each elements in list,
// also the position of acc and i will get interchange in both the left and right fold

val nameList= List("Doe,John","Doe,Jane","Bourne,Jason")

val firstNameLastNameFormat = nameList.foldLeft(List[String]()){ (acc,i)=>
 val fullNameSplit = i.split(",")
 acc :+ fullNameSplit(1)+" "+fullNameSplit(0)
}
//firstNameLastNameFormat: List[String] = List(John Doe, Jane Doe, Jason Bourne)

val firstNameLastNameFormat = nameList.foldRight(List[String]()){ (i,acc)=>
 val fullNameSplit = i.split(",")
 acc :+ fullNameSplit(1)+" "+fullNameSplit(0)
}
//firstNameLastNameFormat: List[String] = List(Jason Bourne, Jane Doe, John Doe)


// foldByKey() same as fold but works on pairedRDD
val maxProductInEachCategory = List(
      ("electronics",("iphone X",1000.0)),
      ("electronics",("samsing s8",900.0)),
      ("shoes",("nike",100.0)),
      ("shoes",("bata",80.0))
    )
//maxProductInEachCategory: List[(String, (String, Double))] = List((electronics,(iphone X,1000.0)), (electronics,(samsing s8,900.0)), (shoes,(nike,100.0)), (shoes,(bata,80.0)))

val maxProductInEachCategoryRDD = sc.makeRDD(maxProductInEachCategory)
//maxProductInEachCategoryRDD: org.apache.spark.rdd.RDD[(String, (String, Double))] = ParallelCollectionRDD[3] at makeRDD at <console>:29

val maxByCategory = maxProductInEachCategoryRDD.foldByKey(("dummy category",0.0)){ ((acc,element) => 
	if(acc._2 > element._2) acc else element)
}
//maxByCategory: org.apache.spark.rdd.RDD[(String, (String, Double))] = ShuffledRDD[4] at foldByKey at <console>:31

println("Max priced product in each category" + maxByCategory.collect().toList)
//Max priced product in each categoryList((electronics,(iphone X,1000.0)), (shoes,(nike,100.0)))
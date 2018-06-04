// combineByKey example 
val KVPair = sc.parallelize( List(("a",1),("a",2),("a",3),("b",1),("b",2),("c",1)) )

val KVPairSum = KVPair.combineByKey(
(initialize) => (initialize, 1),
(acc: (Int, Int), initialize) => (acc._1 + initialize, acc._2 + 1),
(acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
).foreach(println)
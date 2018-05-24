// Scala: sortBy, sortWith, sorted
// Scala: difference between filter and takeWhie
// Scala: difference between Iterator and Iterable
// Spark: getting top n priced products for each category on retail_db/product dataset

// sorted =>> natural ordering on a  list 
val sortSeq = Seq(1,4,5,2,3)
sortSeq.sorted
//res8: Seq[Int] = List(1, 2, 3, 4, 5)

// sortBy ==> sorting based on some attribute
case class Result( val name: String, val marks: Int)
val resultSeq = Seq(Result("John",10),Result("Jane",40),Result("Tom",50),Result("Jerry",20),Result("Popeye",30))
resultSeq.sortBy(._marks)
//res21: Seq[Result] = List(Result(Jane,40), Result(Jerry,20), Result(John,10), Result(Popeye,30), Result(Tom,50))
resultSeq.sortBy(._name)
//res22: Seq[Result] = List(Result(John,10), Result(Jerry,20), Result(Popeye,30), Result(Jane,40), Result(Tom,50))

// sortWith ==> sorting based on a comparator function for custom sorting ( desc sorting odf marks)
resultSeq.sortWith((leftElem,rightElem) => leftElem.marks > rightElem.marks)
resultSeq.sortWith(_.marks > _.marks)
//res38: Seq[Result] = List(Result(Tom,50), Result(Jane,40), Result(Popeye,30), Result(Jerry,20), Result(John,10))

//take(n) gives the first n elements 
List(3, 2, 1, 4).take(3)
res44: List[Int] = List(3, 2, 1)

// filter operates on all the elements wand retuns the element satisying the condition
List(1, 2, 3, 4).filter(_ % 2 == 1)
//res41: List[Int] = List(1, 3)

// takeWhile() stop evaluation immediately after condition is not met.
List(1, 2, 3, 4).takeWhile(_ % 2 == 1)
//res40: List[Int] = List(1)

// Iterable is a stateless which extends collection trait ; the trait implements foreach method using iterator,
// whereas Iterator is a data structure having state and has hasNext and next method to iterate over sequence of elements
// While it is often used to iterate through the elements of a collection, 
// it can also be used without being backed by any collection (see constructors on the companion object).

val iterable: Iterable[Int] = 1 to 5
//iterable: Iterable[Int] = Range(1, 2, 3, 4, 5)
iterable.take(2)
//res45: Iterable[Int] = Range(1, 2)
iterable.take(2)
//res50: Iterable[Int] = Range(1, 2)
val iterator = iterable.iterator
//iterator: Iterator[Int] = non-empty iterator
iterator.next()
//res46: Int = 1
iterator.next()
//res47: Int = 2
if (iterator.hasNext) iterator.next()
//res49: AnyVal = 3


// Get top n priced products within each category
val products = sc.textFile("/user/cloudera/dataset/retail_db/products/")
val productsMap = products.
filter( product => product.split(",")(4) !="").
map(product => (product.split(",")(1).toInt, product)) // categoryid, product
val productsGroupByCategory = productsMap.groupByKey()


// scala based APIs to calulate topN product
def getTopNPricedProducts(productsIterable: Iterable[String], topN: Int): Iterable[String] ={
	//get all unique prices
	val productPrices = productsIterable.map( p=> p.split(",")(4).toFloat).toSet
	//get the top n prices from unique set
	val topNPrices=productPrices.toList.sortBy(p => -p).take(topN)
	//get the minumum price from top n to create a base condition for prices
	val minOfTopNPrices = topNPrices.min
	// products should be sorted by price in ddesc
	val productsSorted=productsIterable.toList.sortBy( product=> -product.split(",")(4).toFloat)
	// apply the min price condition to sorted products to filter out the top n products
	val topNPricedProducts=productsSorted.takeWhile(product=> product.split(",")(4).toFloat >= minOfTopNPrices)
	topNPricedProducts

}

val getTopNPricedProductsByCategory = productsGroupByCategory.flatMap( rec => getTopNPricedProducts(rec._2,3))
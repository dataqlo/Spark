// Scala Collections
// scala.collection.immutable -> never changes after it is created
// scala.collection.mutable -> contains some operations that changes collection in place.

// collections at high level begins with Traversable an iterable which classfies into sequences seq,set and maps
// Traversable  has one abstract method foreach 
// Iterable has an abstract method Iterator, next method can be called 


// sequence is divided into indexedSeq and linerSeq
// IndexedSeq : efficient in random access of element eg Array. Creating indexedSeq creates a vector
val indexSequence = IndexedSeq.(1,2,3)
//indexSequence: IndexedSeq[Int] = Vector(1, 2, 3)

// LinearSeq : can be slpit into head, tail componemt with head, tail and isEmpty methods. Creating linerSeq creates a List
val seq = scala.collection.immutable.LinearSeq(1,2,3)
//seq: scala.collection.immutable.LinearSeq[Int] = List(1, 2, 3)


//Map -> KV Pairs
// HashMap, SortedMap, ListMap, WeakHashMap, TreeMap, LinkedHashMap
val m = Map(1 -> "a", 2 -> "b")
//m: scala.collection.immutable.Map[Int,java.lang.String] = Map(1 -> a, 2 -> b)

//Sets : contains unique elements
// BitSet, HashSet, ListSet, SortedSet, TreeSet
val set = Set(1, 2, 3)
//set: scala.collection.immutable.Set[Int] = Set(1, 2, 3)

// Additional Classes
// Stream, Queus, Stack and Range
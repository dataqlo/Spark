// Object Oriented Programming in scala

// class
// class ClassName { ... } OR class ClassName(constructor) {......}
 
class ClassName(constructor: String) {
val classMember = "Hi, " 
val field1 = constructor

 def someMethod(field2: String) {
  println(classMember+field1+field2)
 }
}

// Objects
// initialize by new keyword

val newObject = new ClassName("Hello")
newObject.someMethod("world")


// Singleton Objects
// scala doesn't have static memmbers
// looks like a class, but have keyword object
// Singleton objects can not take parameters as they do not instantiate.



object Car {
	val OneHPWatt = 745.7
	def HorsePower2Watt(hp: Double): Double = { hp * OneHPWatt}
}

Car.HorsePower2Watt(2)
//res1: Double = 1491.4


// Companion Object
// When Singleton object shares the same name that of class
// both can access each other's private members

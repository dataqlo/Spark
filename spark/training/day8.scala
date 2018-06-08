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

// :paste mode or Car.scala
class Car {
	private val OneHPWatt = 745.7
}

object Car {

	def HorsePower2Watt(carObj: Car,hp: Double) = { carObj.OneHPWatt * hp }
}

val carObj = new Car
println(Car.HorsePower2Watt(carObj,2))

// ctrl+D

// Abstract Class: class may have abstract members which do not have implementation,
// as a result we cannot instantiate abstract class
// forebidden to define same field name and methdo name in same class
// Name Space values (fields, methods, packages, and singleton objects) and types (class and trait names)
// you wantto ensure that a member cannot be overidden by subclasses use final

abstract class Car{
	def model: String // abstract declare
	def make: Int // abstract declare
	def tyre: Int = 4
	def getModel { println("Model: "+model)} // concrete method
	def getMake { println("Make "+make)}
	final def getTyre {println("Tyres: Michellin "+tyre)}
}


class Cars(model1: String, make1 :Int) extends Car {
	def model = model1
	def make = make1

	override def getModel { println(" Car Model: "+model1)}
}


val carsObj = new Cars("BMW",1990)
carsObj.getMake
//Make 1990
carsObj.getModel
//Car Model:BMW
carsObj.getTyre
//Tyres: Michellin 4


// Every class is a subclass of Any.
// Methods defined in Any are universal
// AnyVal is he super class of build-in scala values
// Null and Nothing are the common subclasses


// Traits
// similar to inteface, used to define method, field definition, object types by defining the 
// signature, unlike java traits are partially implemented in scala. They do not have constructor

trait Speed {
	private var speed = 0
	def getSpeed(spd: Int) { println("Speed: "+speed)}
}

trait Fuel {
	private var fuelGuage = 0
	def getFuel { println("Fuel Guage: "+fuelGuage)}
}

// if only single triat use extends and it multiple use with subsequently for each traits
class Car( model:String) extends Speed with Fuel {
	def getModel { println("Model : "+model)}
	override def getSpeed(spd:Int) {  println("Speed: "+spd) } 
}

val carObj = new Car("BMW")
carObj.getSpeed(100)
//Speed: 100
carObj.getFuel
//Fuel Guage: 0


// Case Class

// 1. good for modelling immutable data 
case class Person( name: String, age:Int, location: String)
//new keyword is not used to instantiate case classes, as they have apply method which takes care of object creation
val person1 = Person("John",20,"CA")
// the parameters in case class are public; val is ued to assign which makes the object immutable, var  is discouraged.
person1.name ="jane"
//<console>:29: error: reassignment to val


// 2. case classes are compared by structure and not by reference. person1 and person2 refers to different object but values are same
val person2 = Person("John",20,"CA")
person1==person2
//res1: Boolean = true

// 3. Copying: create a copy of instance by using copy. The name of person2 is used for person3, but it also copied the age and location.
val person3 = person2.copy(name =person2.name)
//person3: Person = Person(John,20,CA)

// 4. pattern matching
abstract class Notification

case class Email(sender: String, title: String) extends Notification
case class SMS(caller: String, message: String) extends Notification


// notification is abstrac' super class has two  concrete case classes, SMS and Email, we can drirectly do 
// patern matching on class using case 
def showNotification(notification: Notification): String = {
  notification match {
    case Email(email, title) =>
      s"You got an email from $email with title: $title"
    case SMS(number, message) =>
      s"You got an SMS from $number! Message: $message"
  }

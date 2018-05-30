234// XML Parsing
import scala.io.Source

//Read raw  xml file and conver to string from Iterator[String]
// Dataset:  https://github.com/AbhishekSolanki/learn/tree/master/dataset/xml/person.xml
val xmlRawFile = Source.fromFile("/home/cloudera/Desktop/dataset/xml/person.xml").getLines.mkString

// convert Raw xml text file to xml
val xmlFile = xml.XML.loadString(xmlRawFile)
//OR
val xmlFile = XML.loadFile("/home/cloudera/Desktop/dataset/xml/person.xml")
//file: scala.xml.Elem

// XPath searching
// \ returns all matching elements in the current node
// \\ returns all matching elements from all the nodes under the current node ( all descendant nodes)

//Get the name tag from xml hierarchy
println( xmlFile \"person" \"personal" \"name")

// city is the children of personal and cannot be accessed via \
xmlFile \"city"
//res11: scala.xml.NodeSeq = NodeSeq()

// to access the child / grand child of current node use \\
xmlFile \\"city"
//res12: scala.xml.NodeSeq = NodeSeq(<city>New York</city>, <city>Chicago</city>)

// NodeSeq is just a sequence of node seq[Node], each inidividual node inside nodeseq is of type element,
// each element contains xml tags and the value can be extracted by text method


// Extracting the attribute from all the person tags,
// if there is only one node then use \"person" \"@id"
val personIDs = (xmlFile \"person").map(_ \"@id")
// res10: scala.collection.immutable.Seq[scala.xml.NodeSeq] = List(1001, 1002)


// get the person node where id = 1001
val personIDs = (xmlFile \"person").map( element =>  
	if((element \"@id").text.toInt == 1001){
		 Some(element)
	} else {
		None
	}
).filter(_ != None)(0)

// using for loop
import util.control.Breaks._
var personWithId1001: scala.xml.Node = null
breakable{
for ( element <- (xmlFile \"person") ){
	
		if( (element \"@id").text.toInt != 1001 ){
			break
		}
	    personWithId1001 = element	
	}
}
println(personWithId1001)
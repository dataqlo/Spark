// xml methods and basic parsing
// scala understand xml and can be assigned to variables without "" and it supports multilines too

val fullName= <Person><FirstName>John</FirstName><LastName>Doe</LastName></Person>
// fullName: scala.xml.Elem = <Person><FirstName>John</FirstName><LastName>Doe</LastName></Person>
//OR
val fullName= <Person>
                <FirstName>John</FirstName>
                <LastName>Doe</LastName>
              </Person>

// If the block of xml is given as string we can convert it to xml by loadString method of Scala.xml.XML object
val fullName = xml.XML.loadString("<Person><FirstName>John</FirstName><LastName>Doe</LastName></Person>")
// fullName: scala.xml.Elem


// Elem, Node, NodeSeq, NodeBuffer, PCDATA, Text, Unparsed


// Generating Dynamic XML
var firstName = "John"
var lastName ="Doe"
var personXML = <Person><FirstName>{firstName}</FirstName><LastName>{lastName}</LastName></Person>
// personXML: scala.xml.Elem = <Person><FirstName>John</FirstName><LastName>Doe</LastName></Person>

val friends = List("John","Jane","Bruce","James","Jason")
val friendsList = <ul>{friends.map(i => <li> {i} </li>)}</ul>
//friendsList: scala.xml.Elem = <ul><li> John </li><li> Jane </li><li> Bruce </li><li> James </li><li> Jason </li></ul>

val friendsListBuffer = new xml.NodeBuffer
//friendsListBuffer: scala.xml.NodeBuffer = ArrayBuffer()
friendsListBuffer += <li>John</li>
friendsListBuffer += <li>Jane</li>
val friendsList = <ul>{friendsListBuffer}</ul>
//friendsList: scala.xml.Elem = <ul><li>John</li><li>Jane</li></ul>
//NodeBuffer is a simple convenience class that extends ArrayBuffer[Node]. It adds one method named &+ that appends the given object to the buffer
val friendsListBuffer = new xml.NodeBuffer
val friendsListArrayBuffer = friendsListBuffer &+ <li>John</li> &+ <li>Jane</li>
//friendsList: scala.xml.NodeBuffer = ArrayBuffer(<li>John</li>, <li>Jane</li>, <li>John</li>, <li>Jane</li>)

// Preety Printing
val p = new scala.xml.PrettyPrinter(80, 4)
p.format(friendsList)
//res3: String = 
//<ul>
//    <li> John </li>
//    <li> Jane </li>
//    <li> Bruce </li>
//    <li> James </li>
//    <li> Jason </li>
//</ul>

// Saving XML
scala.xml.XML.save("FriendsList.xml", friendsList)
scala.xml.XML.save("FriendsList.xml", friendsList,"UTF-8", true, null)


// xml load url
val xml = XML.load("http://www.devdaily.com/rss.xml")

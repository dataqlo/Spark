/*
 * Author: https://github.com/AbhishekSolanki
 * Date: 14th July 2018
 * Description: contains luhn algorithm for card number validation and data processing method based on record type
*/
import scala.collection.immutable.List
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

// Serializable is required for class otherwise "Task not Serializable: java.io.NotSerializableException" will occur
@SerialVersionUID(123L)
class Algorithms  extends Serializable{

  // validates the card number based on luhn algorithm and returns the string output "valid" / "invalid"
	def luhnAlgoCardValidation(cardNumber:String) : String = {
	  val originalCardNumber = cardNumber.reverse.toList
		var sumOfCardNumber = 0
		// zipWithIndex method returns (element,index) for list
		originalCardNumber.zipWithIndex.foreach{
	    case (elem,index) => {
	      if(index==0) sumOfCardNumber+=elem.toString.toInt
	      else
	        if(index%2!=0){
	          sumOfCardNumber += (elem.asDigit*2).toString.toList.map(_.toString.toInt).sum
	          }
	        else {
	          sumOfCardNumber += elem.toString.toInt
					}
				}
	    }
	  if ( sumOfCardNumber % 10 == 0 ) "valid" else "invalid"
	 }
	
	// process the data received from kafka queue, based of format it returns List(IIN:String, cardNumber:String, valid:String)
	def dataProcess(dataPayLoad:String): List[String] = {
	  if ( dataPayLoad.charAt(0) == '<' ) {
	    //	XML data
			val xmlDataPayLoad = xml.XML.loadString(dataPayLoad)
			val IIN = (xmlDataPayLoad \\"IssuingNetwork").text
			val cardNumber = (xmlDataPayLoad \\"CardNumber").text
			val valid = this.luhnAlgoCardValidation(cardNumber)
			List(IIN,cardNumber,valid)
			}
	  else if ( dataPayLoad.charAt(0) == '[') {
	    // JSON data
	    // for parsing json external Lift-Json library is used with XPATH option
			val json = parse(dataPayLoad)
			val IIN = compactRender(json\"CreditCard"\"IssuingNetwork").replace("\"", "")
			val cardNumber = compactRender(json\"CreditCard"\"CardNumber")
			val valid = this.luhnAlgoCardValidation(cardNumber)
			List(IIN,cardNumber,valid)
			} 
	  else {
			// CSV data
			val csvDataPayLoad = dataPayLoad.split(",")
			val IIN = csvDataPayLoad(0)
			val cardNumber = csvDataPayLoad(1)
			val valid = this.luhnAlgoCardValidation(cardNumber)
			List(IIN,cardNumber,valid)
		}
	}
}
import scala.collection.immutable.List
import net.liftweb.json._
import net.liftweb.json.JsonDSL._


@SerialVersionUID(123L)
class Algorithms  extends Serializable{

	def luhmAlgoCardValidation(cardNumber:String) : String = {
		val originalCardNumber = cardNumber.reverse.toList
				var sumOfCardNumber = 0
				originalCardNumber.zipWithIndex.foreach{
				case (elem,index) => {
					if(index==0) sumOfCardNumber+=elem.toString.toInt
							else
								if(index%2!=0){
									sumOfCardNumber += (elem.asDigit*2).toString.toList.map(_.toString.toInt).sum
								} else {
									sumOfCardNumber += elem.toString.toInt
								}
				}}
		if ( sumOfCardNumber % 10 == 0 ) "valid" else "invalid"
	}
	
	def dataProcess(dataPayLoad:String): List[String] = {
	  if ( dataPayLoad.charAt(0) == '<' ) {
						//	XML data
						val xmlDataPayLoad = xml.XML.loadString(dataPayLoad)
								val IIN = (xmlDataPayLoad \\"IssuingNetwork").text
								val cardNumber = (xmlDataPayLoad \\"CardNumber").text
								val valid = this.luhmAlgoCardValidation(cardNumber)
								List(IIN,cardNumber,valid)
					} else if ( dataPayLoad.charAt(0) == '[') {
						// JSON data
						val json = parse(dataPayLoad)
								val IIN = compactRender(json\"CreditCard"\"IssuingNetwork").replace("\"", "")
								val cardNumber = compactRender(json\"CreditCard"\"CardNumber")
								val valid = this.luhmAlgoCardValidation(cardNumber)
								List(IIN,cardNumber,valid)
					} else {
						// CSV data
						val csvDataPayLoad = dataPayLoad.split(",")
								val IIN = csvDataPayLoad(0)
								val cardNumber = csvDataPayLoad(1)
								val valid = this.luhmAlgoCardValidation(cardNumber)
								List(IIN,cardNumber,valid)
					}
	}
}
// Luhn algorithm development for scala to validate the card no is valid or not
val originalCardNumber = "4111111111111111".reverse.toList
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
if ( sumOfCardNumber % 10 == 0 ) print("valid") else print("invalid")



// Luhn algorithm implementation for spark scala used in spark streaming, the data will be received via kafka
//American Express, Diners Club, Discover, JCB, MasterCard, Visa
val originalCardNumberList = List ("371449635398431","30569309025904","6011111111111117","3530111333300000","5555555555554444","4111111111111111")
//Luhn algorithm
originalCardNumberList.map(cardno => {
var sumOfCardNumber = 0
val originalCardNumber = cardno.toString.reverse.toList
originalCardNumber.zipWithIndex.foreach{
case (elem,index) => {
 if(index==0) sumOfCardNumber+=elem.asDigit
	else
		if(index%2!=0){
			sumOfCardNumber += (elem.asDigit*2).toString.toList.map(_.toString.toInt).sum
		} else {
			sumOfCardNumber += elem.asDigit
		}
}}
if ( sumOfCardNumber % 10 == 0 )
 (cardno,"valid") 
else (cardno,"invalid")
}).foreach(println)
Bank of Data
----

### Overview

BankOfData issues AmEx,MasterCard and Visa credit cards to its customer. There are 29,997 cards in addition it has 33 fake cards in circulation. 
* 9999 * 3 ( AmEx + Mastercard + Visa ) = 29997
* Fake Cards = 33
* Total dataset = 30030 , fake 0.1098 % 


### Setting Up
- Install Tomcat ( [Ref.](https://github.com/AbhishekSolanki/Spark/tree/master/setup) set TOMCAT_DOWNLOAD=Y and TOMCAT_INSTALL=Y in components.cfg )
- Move source/BOD.war to Tomcat webapps folder ( /home/cloudera/apache-tomcat-8.5.31/webapps/ )
- Start Tomcat server ( catalina.sh start )
- Test the BOD data via http://localhost:8080/BOD/rest/Transaction/data/ and check the api is accessible

### Running
- source will generate data AmEx, Mastercard and Visa data in CSV,XML,JSON in round robbin fashion
- fetch_data.sh : pools the data from the rest api and segregate the data based on the card type. 
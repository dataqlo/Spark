VM Migrate & Component Installation
----------

### Setting Up
- create directory "setup" in /home/cloudera/setup
- create directory "downloads" in setup dir i.e /home/cloudera/setup/downloads
- download component.cfg and vm_migrate.sh in setup directory

### Getting Started 
- all the component dependies which are needs to be download and install are defined in component.cfg<br>
ones the component is downloaded and installed mark the _DOWNLOAD and _INSTALL from Y to N<br>
to reinstall again keep _DOWNLOAD=N amd _INSTALL=Y
- the packages will be download into "downloads" directory 
- by default the components will be extracted to /home/cloudera/
- this will edit your current ~/.bashrc file and all the components will be added 

### Components
- Java 1.8.172
- Sublime Text 3
- Scala 2.12.6
- Scala Eclipse IDE 4.7.0
- Apache Maven 3.5.4
- Elasticsearch 6.3.0
- Apache Tomcat 8.5.31
- Apache Kafka 0.10.2
- Python 2.7, PIP2.7
- kafka-python library
#!/bin/bash
set -x
baseDir=$(dirname "`readlink -f "$0"`")
downloadDir=${baseDir}/downloads
githubDir=$HOME/github
. $baseDir/components.cfg
sed -i '/#CLOUDERA_DEFAULTS added by dataqlo.com/d' ~/.bashrc
echo "CLOUDERA_DEFAULTS=/usr/local/apache-ant/apache-ant-1.9.2/bin:/usr/local/bin:/usr/bin:/bin:/usr/local/sbin:/usr/sbin:/sbin:/home/cloudera/bin #CLOUDERA_DEFAULTS added by dataqlo.com" >> ~/.bashrc


function fDownloadComponents {

 if [ $JAVA_DOWNLOAD == "Y" ]; then yes "y" | wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" $JAVA_DOWNLOAD_URL -P $downloadDir/; fi
 if [ $SCALA_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $SCALA_DOWNLOAD_URL -P $downloadDir/; fi
 if [ $SCALA_ECLIPSE_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $SCALA_ECLIPSE_DOWNLOAD_URL -P $downloadDir/; fi
 if [ $ELASTICSEARCH_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $ELASTICSEARCH_DOWNLOAD_URL -P $downloadDir/; fi
 if [ $MAVEN_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $MAVEN_DOWNLOAD_URL -P $downloadDir/; fi
 if [ $TOMCAT_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $TOMCAT_DOWNLOAD_URL -P $downloadDir/; fi
 if [ $MARKLOGIC_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $MARKLOGIC_DOWNLOAD_URL -P $downloadDir/; fi 
 if [ $PYTHON27_DOWNLOAD == "Y" ]; then yes "y" | wget --no-check-certificate $PYTHON27_DOWNLOAD_URL -P $downloadDir/; yes "y" | wget https://bootstrap.pypa.io/get-pip.py -P $downloadDir/; fi 

}

function fInstallComponents {
# Sublime text Installation
 if [ $SUBLIME_INSTALL == "Y" ]
 then
  sudo rpm -v --import $SUBLIME_RPM_V_IMPORT
  sudo yum-config-manager --add-repo $SUBLIME_REPO
  yes "y" | sudo yum install sublime-text
 fi
# Java Installation
 if [ $JAVA_INSTALL == "Y"  ]
 then
  sudo tar -xzvf $downloadDir/${JAVA_DOWNLOAD_URL##*/} -C $JAVA_INSTALL_DIR
  sed -i '/#JAVA_HOME added by dataqlo.com/d' ~/.bashrc 
  echo "JAVA_HOME=${JAVA_INSTALL_DIR}/jdk${JAVA_VERSION} #JAVA_HOME added by dataqlo.com" >> ~/.bashrc
 fi
# Scala Installation
 if [ $SCALA_INSTALL == "Y" ]
 then
   yes "y" | sudo yum install $downloadDir/${SCALA_DOWNLOAD_URL##*/}
   sed -i '/#SCALA_HOME added by dataqlo.com/d' ~/.bashrc 
   echo "SCALA_HOME=/usr/share/scala #SCALA_HOME added by dataqlo.com" >> ~/.bashr
 fi
 
 if [ $SCALA_ECLIPSE_INSTALL == "Y" ]
 then
  mkdir -p $SCALA_ECLIPSE_INSTALL_DIR
  tar -xzvf $downloadDir/${SCALA_ECLIPSE_DOWNLOAD_URL##*/} -C $SCALA_ECLIPSE_INSTALL_DIR
 fi

 if [ $ELASTICSEARCH_INSTALL == "Y" ]
 then
 	sed -i '/#ELASTICSEARCH_HOME added by dataqlo.com/d' ~/.bashrc 
    echo "ELASTICSEARCH_HOME=${ELASTICSEARCH_INSTALL_DIR}/elasticsearch-${ELASTICSEARCH_VERSION} #ELASTICSEARCH_HOME added by dataqlo.com" >> ~/.bashrc
 	tar -xzvf $downloadDir/${ELASTICSEARCH_DOWNLOAD_URL##*/} -C $ELASTICSEARCH_INSTALL_DIR
 fi

 if [ $MAVEN_INSTALL == "Y" ]
 then
 	sed -i '/#M2_HOME added by dataqlo.com/d' ~/.bashrc 
    echo "M2_HOME=${MAVEN_INSTALL_DIR}/apache-maven-${MAVEN_VERSION} #M2_HOME added by dataqlo.com" >> ~/.bashrc
 	tar -xzvf $downloadDir/${MAVEN_DOWNLOAD_URL##*/} -C $MAVEN_INSTALL_DIR
 fi

 if [ $TOMCAT_INSTALL == "Y" ]
 then
 	sed -i '/#CATALINA_HOME added by dataqlo.com/d' ~/.bashrc 
    echo "CATALINA_HOME=${TOMCAT_INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION} #CATALINA_HOME added by dataqlo.com" >> ~/.bashrc
 	tar -xzvf $downloadDir/${TOMCAT_DOWNLOAD_URL##*/} -C $TOMCAT_INSTALL_DIR
 fi

 if [ $MARKLOGIC_INSTALL == "Y" ]
 then
  #sed -i '/#CATALINA_HOME added by dataqlo.com/d' ~/.bashrc 
   # echo "CATALINA_HOME=${TOMCAT_INSTALL_DIR}/apache-tomcat-${TOMCAT_VERSION} #CATALINA_HOME added by dataqlo.com" >> ~/.bashrc
   yes "y" | sudo yum install $downloadDir/${MARKLOGIC_DOWNLOAD_URL##*/}
 fi

 if [ $KAFKA_INSTALL == "Y" ]
 then
  sed -i '/#KAFKA_HOME added by dataqlo.com/d' ~/.bashrc 
  echo "KAFKA_HOME=/usr/lib/kafka #KAFKA_HOME added by dataqlo.com" >> ~/.bashrc
   yes "y" | sudo yum install kafka
 fi

 if [ $PYTHON27_INSTALL == "Y" ]
 then
 	yes "y" | sudo yum install gcc
 	sudo tar -xzf  $downloadDir/Python-2.7.10.tgz -C $PYTHON27_INSTALL_DIR
 	cd $PYTHON27_INSTALL_DIR/Python-2.7.10/
 	sudo ./configure
 	sudo make altinstall
 	cd -
    sudo python2.7 $downloadDir/get-pip.py
    sudo sed -i -e 's/#!/#!\/usr\/local\/bin\/python2.7/g' /usr/local/bin/pip2.7
 fi

 if [ $PIP27_DEPENDENCIES == "Y" ]
 then
 	sudo pip2.7 install kafka-python
 fi



}

fDownloadComponents
fInstallComponents


# adding PATH in profile and refreshing env variables
sed -i '/#PATH added by dataqlo.com/d' ~/.bashrc
echo "PATH=\$JAVA_HOME/bin:\$CLOUDERA_DEFAULTS:\$SCALA_HOME/bin:\$ELASTICSEARCH_HOME/bin:\$M2_HOME/bin:\$CATALINA_HOME/bin #PATH added by dataqlo.com" >> ~/.bashrc
source ~/.bashrc

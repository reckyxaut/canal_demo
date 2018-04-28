echo start appication...... 
echo off

#echo $CLASSPATH
APP_HOME=`pwd`
export CLASSPATH=.:$CLASSPATH:$APP_HOME/resources:$APP_HOME
#echo CLASSPATH:$CLASSPATH
#echo APP_HOME:$APP_HOME

EXT_DIRS=./:$JAVA_HOME/jre/lib/ext

java -Dfile.encoding=UTF-8 -Djava.ext.dirs=./:$JAVA_HOME/jre/lib/ext  -jar proxy-app-lnt-1.0.0.jar -Xms512m -Xmx1024m -Xmn128m -XX:NewRatio=4 -XX:MaxPermSize=512m -XX:+UseParallelGC -XX:ParallelGCThreads=2 -XX:MaxGCPauseMillis=100


 repo sync -f -j10  

 while [ $? == 1 ]; do  
 echo start appication failed,try again......
 sleep 3  
 repo sync -f -j10  

 done

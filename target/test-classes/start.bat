@title �������ƽ̨-�ۺ�ǰ��ϵͳ  
echo off
echo ϵͳ����������......
rem  echo current cd is:%CD%
set APP_HOME=%CD%
rem  echo APP_HOME is %APP_HOME%  

set classpath=%classpath%;%APP_HOME%\resources;%APP_HOME%;
echo  CLASS_PATH is:%classpath%

rem  set mydir=.;%CD%;%JAVA_HOME%/jre/lib/ext;
rem  echo mydir:%mydir%

rem  echo ext.dirs:./;%JAVA_HOME%/jre/lib/ext

set appjar=proxy-app-lnt-1.0.0.jar
set appjvm=-Xms512m -Xmx1024m -Xmn60m -XX:NewRatio=4 -XX:MaxPermSize=500m -XX:+UseParallelGC -XX:ParallelGCThreads=2 -XX:MaxGCPauseMillis=100


java -Dfile.encoding=UTF-8  -Djava.ext.dirs=./   %appjvm%  -jar  %appjar%

call start.bat
#pause

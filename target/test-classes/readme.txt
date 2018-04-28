1、安装canal，配置canal.properties,instance.properties
2、配置mysql数据库，开启binlog：
	log-bin=mysql-bin #添加这一行就ok
	binlog-format=ROW #选择row模式，虽然Canal支持各种模式，但是想用otter，必须用ROW模式
	server_id=1 #配置mysql replaction需要定义，不能和canal的slaveId重复
3、添加数据库Canal用户，并授权：
	CREATE USER canal IDENTIFIED BY 'canal';  
	GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
	FLUSH PRIVILEGES;
4、启动canal
5、配置canal客户端应用程序参数，并启动canal客户端程序	
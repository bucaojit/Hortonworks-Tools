$ sbt clean package

current dir: /home/oliver/Hortonworks-Tools/spark-tools/hbase-auth-work

$ /usr/hdp/current/spark-client/bin/spark-submit --class HBaseRead --master yarn --jars /usr/hdp/current/hbase-client/lib/guava-12.0.1.jar,/usr/hdp/current/hbase-client/lib/hbase-common.jar,/usr/hdp/current/hbase-client/lib/hbase-client.jar,/usr/hdp/current/hbase-client/lib/hbase-protocol.jar,/usr/hdp/current/hbase-client/lib/hbase-server.jar target/scala-2.10/scalatest_2.10-1.0.jar

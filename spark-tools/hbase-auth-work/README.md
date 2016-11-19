$ sbt clean package

current dir: /home/oliver/Hortonworks-Tools/spark-tools/hbase-auth-work

$ /usr/hdp/current/spark-client/bin/spark-submit --class HBaseRead --master yarn --jars /usr/hdp/current/hbase-client/lib/hbase-common.jar --driver-class-path /usr/hdp/current/hbase-client/lib/hbase-common.jar target/scala-2.10/scalatest_2.10-1.0.jar
